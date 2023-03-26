package com.weatherapp.network.data

import com.weatherapp.util.helper.SingleBlock

sealed class ResultData<T> {
    internal class Data<T> internal constructor(val data: T) : ResultData<T>()
    internal class Message<T> internal constructor(val data: MessageData) : ResultData<T>()

    fun isMessageData(): Boolean = this is Message
    fun getMessageOrNull(): String? = (this as? Message)?.data?.getMessageOrNull()
    fun getResourceOrNull(): Int? = (this as? Message)?.data?.getResourceOrNull()

    fun getMessageDataOrNull(): MessageData? = (this as? Message)?.data
    fun getDataOrNull() = (this as? Data<T>)?.data

    inline fun onMessage(f: SingleBlock<String>): ResultData<T> {
        getMessageOrNull()?.let { f(it) }
        return this
    }

    inline fun onResource(f: SingleBlock<Int>): ResultData<T> {
        getResourceOrNull()?.let { f(it) }
        return this
    }

    inline fun onData(f: SingleBlock<T>): ResultData<T> {
        getDataOrNull()?.let { f(it) }
        return this
    }

    inline fun onMessageData(f: SingleBlock<MessageData>): ResultData<T> {
        if (isMessageData()) {
            getMessageDataOrNull()?.let { f(it) }
        }
        return this
    }

    companion object {
        fun <T> messageData(data: MessageData): ResultData<T> = Message(data)
        fun <T> data(data: T): ResultData<T> = Data(data)
    }
}