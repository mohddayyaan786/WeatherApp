package com.weatherapp.network

import com.weatherapp.R
import com.weatherapp.network.data.MessageData
import com.weatherapp.network.data.ResultData
import com.weatherapp.util.extension.loge
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): ResultData<T> {
        return try {
            val response = call.invoke()

            if (response.code() in 400..500) {
                return if (response.code() == 401) {
                    ResultData.messageData(MessageData.expiredToken(response.code()))
                } else {
                    val json = JSONObject(response.errorBody()?.string() ?: "")
                    val message = json.get("message").toString()
                    ResultData.messageData(MessageData.message(message))
                }
            }
            if (response.body() == null) ResultData.messageData(MessageData.resource(R.string.error_app))
            else ResultData.data(response.body()!!)
        } catch (throwable: Throwable) {
            "SafeApiRequest ex: ${throwable.message.toString()}".loge()

            val res = when (throwable) {
                is IOException, is HttpException -> R.string.error_connection
                else -> R.string.error_app
            }
            ResultData.messageData(MessageData.resource(res))
        }
    }
}