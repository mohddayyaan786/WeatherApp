package com.weatherapp.network.data

import androidx.annotation.StringRes
import com.weatherapp.util.helper.SingleBlock

sealed class MessageData {

    internal class ExpiredToken internal constructor(val code: Int) : MessageData()
    internal class Resource internal constructor(@StringRes val res: Int) : MessageData()
    internal class Message internal constructor(val message: String) : MessageData()

    fun isMessage() = this is Message
    fun isResource() = this is Resource

    fun getMessageOrNull(): String? = (this as? Message)?.message
    fun getResourceOrNull(): Int? = (this as? Resource)?.res

    inline fun onMessage(f: SingleBlock<String>): MessageData {
        if (isMessage()) getMessageOrNull()?.let { f(it) }
        return this
    }

    inline fun onResource(f: SingleBlock<Int>): MessageData {
        if (isResource()) getResourceOrNull()?.let { f(it) }
        return this
    }

    companion object {
        fun expiredToken(token: Int): MessageData = ExpiredToken(token)
        fun message(message: String): MessageData = Message(message)
        fun resource(@StringRes res: Int): MessageData = Resource(res)
    }
}