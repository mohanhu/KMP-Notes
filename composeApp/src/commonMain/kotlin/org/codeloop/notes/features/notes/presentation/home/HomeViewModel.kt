package org.codeloop.notes.features.notes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeloop.notes.features.notes.domain.model.NotesItem
import org.codeloop.notes.features.notes.domain.repository.NotesLocalRepository
import org.codeloop.notes.utils.loadstate.LoadState
import org.codeloop.notes.utils.loadstate.LoadStates
import org.codeloop.notes.utils.loadstate.LoadType

class HomeViewModel(
    private val notesLocalRepository: NotesLocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept : (HomeUiAction) -> Unit = ::onUiAction

    private fun onUiAction(action : HomeUiAction) {
        when(action) {

            else -> {}
        }
    }

    private fun sendUiEvent(event : HomeUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun setLoading(
        loadType: LoadType,
        loadState: LoadState
    ) {
        _uiState.update {
            it.copy(
                loadStates = it.loadStates.modify(
                    loadType = loadType,
                    loadState = loadState
                )
            )
        }
    }
}


data class HomeUiState(
    val loadStates: LoadStates = LoadStates.IDLE,
    val notesList : List<NotesItem> = listOf()
)

sealed interface HomeUiAction {

}

sealed interface HomeUiEvent {

}