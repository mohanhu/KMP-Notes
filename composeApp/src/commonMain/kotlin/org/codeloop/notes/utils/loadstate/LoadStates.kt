package org.codeloop.notes.utils.loadstate

enum class LoadType {
    REFRESH,
    APPEND,
    PREPEND,
    ACTION
}

sealed class LoadState(
    val endOfPaginationReached : Boolean = false,
) {

    class Loading  : LoadState(endOfPaginationReached = false)

    class NotLoading(
        endOfPaginationReached: Boolean
    ) : LoadState(endOfPaginationReached = endOfPaginationReached) {
        companion object {
            val Complete = NotLoading(endOfPaginationReached = true)
            val Incomplete = NotLoading(endOfPaginationReached = false)
        }
    }

    class Error(
        val error : Throwable
    ) : LoadState(
        endOfPaginationReached = false
    )
}

data class LoadStates(
    val refresh : LoadState,
    val prepend : LoadState,
    val append : LoadState,
    val action : LoadState,
) {

    fun modify(loadType: LoadType, loadState: LoadState): LoadStates {
        return when(loadType) {
            LoadType.REFRESH -> {
                copy(
                    refresh = loadState
                )
            }
            LoadType.APPEND -> {
                copy(
                    append = loadState
                )
            }
            LoadType.PREPEND -> {
                copy(
                    prepend = loadState
                )
            }

            LoadType.ACTION -> {
                copy(
                    action = loadState
                )
            }
        }
    }

    companion object {
        val IDLE = LoadStates(
            refresh = LoadState.NotLoading.Incomplete,
            prepend = LoadState.NotLoading.Incomplete,
            append = LoadState.NotLoading.Incomplete,
            action = LoadState.NotLoading.Incomplete
        )
    }
}