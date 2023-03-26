package com.weatherapp.util.extension

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.weatherapp.databinding.ContentLoadingBinding
import com.weatherapp.util.helper.EmptyBlock
fun LottieAnimationView.showHide(beVisible: Boolean) {
    if (beVisible) {
        beVisible()
        playAnimation()
    } else {
        resumeAnimation()
        beGone()
    }
}
fun ContentLoadingBinding.error(message: String) {
    layoutEmpty.beGone()
    textError.text = message
    layoutError.beVisible()
}
fun ContentLoadingBinding.loading(isLoading: Boolean) {
    layoutError.beGone()
    indicator.showHide(isLoading)
}
fun ContentLoadingBinding.empty(count: Int = 0) {
    layoutEmpty.beVisibleIf(count == 0)
}
fun ContentLoadingBinding.retry(onRetry: EmptyBlock) {
    buttonRetry.setOnClickListener { onRetry() }
}
fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()
fun View.beVisible() {
    visibility = View.VISIBLE
}
fun View.beGone() {
    visibility = View.GONE
}




