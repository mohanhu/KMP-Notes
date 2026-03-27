package org.codeloop.notes.utils.loadstate

sealed class ResourceResult<out R> {
    data class Success<R>(val data: R) : ResourceResult<R>()
    data class Error(val exception: Throwable) : ResourceResult<Nothing>()
    object Loading : ResourceResult<Nothing>()
}