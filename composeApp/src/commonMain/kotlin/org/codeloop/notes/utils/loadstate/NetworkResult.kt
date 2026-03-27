package org.codeloop.notes.utils.loadstate

sealed class NetworkResult<T> (
    val data : T ?= null,
    open val message : String ?= null,
    open val code : Int ?= null,
    open val uiMessage : String ?= null
) {
    class Loading<T> : NetworkResult<T>()
    class Error<T>(
        val error : Throwable,
        override val message: String,
        override val code: Int = -1,
        override val uiMessage: String? = null
    ) : NetworkResult<T>(message = message, code = code, uiMessage = uiMessage)

    class Success<T>(
        data: T,
        message: String?,
        override val code: Int = -1,
    ) : NetworkResult<T>(data = data)
}