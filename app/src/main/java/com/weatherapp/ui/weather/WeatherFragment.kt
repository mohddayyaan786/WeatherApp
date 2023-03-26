package com.weatherapp.ui.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.weatherapp.R
import com.weatherapp.databinding.FragmentWeatherBinding
import com.weatherapp.ui.BaseFragment
import com.weatherapp.ui.weather.data.response.WeatherResponse
import com.weatherapp.util.APP_ID
import com.weatherapp.util.LOCATION_REQUEST_CODE
import com.weatherapp.util.currentDate
import com.weatherapp.util.extension.*
import com.weatherapp.util.kelvinToCentigrade
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherViewModel>() {

    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    override val viewModelClass = WeatherViewModel::class.java

    override fun viewBindingInflate(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentWeatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        with(binding) {

            with(viewModel) {
                uiState().observe(viewLifecycleOwner) { state -> onLoadState(state) }
                loading.retry {
                    if (getIsUserSearchedCity() && !TextUtils.isEmpty(citySearch.text.toString())) getCityWeatherInfo(
                        citySearch.text.toString(),
                        APP_ID
                    )
                    else getCurrentLocation()
                }
                getCurrentLocation()
                citySearch.onEditorActionListener {
                    if (!TextUtils.isEmpty(citySearch.text.toString())) getCityWeatherInfo(
                        this,
                        APP_ID
                    )
                    else Toast.makeText(
                        requireContext(),
                        getString(R.string.please_enter_city_name),
                        Toast.LENGTH_LONG
                    ).show()
                }
                currentLocation.setOnClickListener { getCurrentLocation() }
                getLocationLiveData().observe(viewLifecycleOwner) {
                    getCurrentWeatherInfo(it.latitude.toString(), it.longitude.toString(), APP_ID)
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProvider.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            viewModel.setLocationLiveData(location)
                        }
                    }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), LOCATION_REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }

    private fun onLoadState(state: WeatherDataResultState) {
        with(binding) {
            when (state) {
                is WeatherDataResultState.Success -> {
                    appBarLayout.beVisible()
                    rootLayout.beVisible()
                    setResults(state.weatherResponse)
                }
                is WeatherDataResultState.Loading -> {
                    appBarLayout.beGone()
                    rootLayout.beGone()
                    loading.loading(state.loading)
                }
                is WeatherDataResultState.ErrorMessage -> {
                    appBarLayout.beGone()
                    rootLayout.beGone()
                    loading.error(state.message)
                }
                is WeatherDataResultState.ErrorRes -> {
                    appBarLayout.beGone()
                    rootLayout.beGone()
                    loading.error(getString(state.res))
                }
            }
        }
    }

    private fun setResults(weatherResponse: WeatherResponse) {
        with(binding) {
            with(weatherResponse) {

                dateTime.text = currentDate()

                maxTemp.text = getString(
                    R.string.temp_format,
                    getString(R.string.max),
                    kelvinToCentigrade(main?.temp_max!!)
                )

                minTemp.text = getString(
                    R.string.temp_format,
                    getString(R.string.min),
                    kelvinToCentigrade(main.temp_min!!)
                )

                temp.text = getString(R.string.temp_format, "", kelvinToCentigrade(main.temp!!))

                weatherTitle.text = weather[0].main

                sunriseValue.milliesToDate(sys.sunrise.toLong())
                sunsetValue.milliesToDate(sys.sunset.toLong())

                pressureValue.text = main.pressure.toString()

                humidityValue.text =
                    getString(R.string.humidity_format, main.humidity, getString(R.string.per))

                tempFValue.text = getString(
                    R.string.temp_format_to_int,
                    "",
                    (kelvinToCentigrade(main.temp).times(1.8)).plus(32).roundToInt()
                )

                citySearch.setText(name)

                feelsLike.text =
                    getString(R.string.temp_format, "", kelvinToCentigrade(main.feels_like))

                windValue.text = getString(R.string.wind_speed_format, wind.speed)

                groundValue.text = main.grnd_level.toString()

                seaValue.text = main.sea_level.toString()

                countryValue.text = sys.country

                updateUI(weather[0].id)
            }
        }

    }

    private fun updateUI(id: Int) {
        binding.apply {
            when (id) {
                //Thunderstorm
                in 200..232 -> {
                    setImageAndBackground(R.drawable.ic_storm_weather, R.drawable.thunderstrom_bg)
                }
                //Drizzle
                in 300..321 -> {
                    setImageAndBackground(R.drawable.ic_few_clouds, R.drawable.drizzle_bg)
                }
                //Rain
                in 500..531 -> {
                    setImageAndBackground(R.drawable.ic_rainy_weather, R.drawable.rain_bg)
                }
                //Snow
                in 600..622 -> {
                    setImageAndBackground(R.drawable.ic_snow_weather, R.drawable.snow_bg)
                }
                //Atmosphere
                in 701..781 -> {
                    setImageAndBackground(R.drawable.ic_broken_clouds, R.drawable.atmosphere_bg)
                }
                //Clear
                800 -> {
                    setImageAndBackground(R.drawable.ic_clear_day, R.drawable.clear_bg)
                }
                //Clouds
                in 801..804 -> {
                    setImageAndBackground(R.drawable.ic_cloudy_weather, R.drawable.clouds_bg)
                }
                //unknown
                else -> {
                    setImageAndBackground(R.drawable.ic_unknown, R.drawable.unknown_bg)
                }
            }
        }
    }

    private fun setImageAndBackground(image: Int, background: Int) {
        with(binding) {
            weatherImg.setImageResource(image)
            mainLayout.background = ContextCompat.getDrawable(requireContext(), background)
            optionsLayout.background = ContextCompat.getDrawable(requireContext(), background)
        }
    }

}