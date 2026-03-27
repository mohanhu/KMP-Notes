package org.codeloop.notes.features.notes.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeloop.notes.features.notes.domain.model.Category
import org.codeloop.notes.features.notes.domain.model.NotesItem
import org.codeloop.notes.features.notes.domain.model.toNotesItem
import org.codeloop.notes.features.notes.domain.repository.NotesLocalRepository
import org.codeloop.notes.utils.loadstate.LoadState
import org.codeloop.notes.utils.loadstate.LoadStates
import org.codeloop.notes.utils.loadstate.LoadType

class NotesListViewModel(
    private val notesLocalRepository: NotesLocalRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesListUiState())
    val uiState = _uiState.asStateFlow()

    val allList = _uiState.map {
        it.toAllUiModel()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value.toAllUiModel()
    )

    val favouritesList = _uiState.map {
        it.toFavouritesUiModel()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value.toFavouritesUiModel()
    )

    private val _uiEvent = MutableSharedFlow<NotesListUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept: (NotesListUiAction) -> Unit = ::onUiAction

    init {

        combine(
            _uiState.map { it.query }.distinctUntilChanged(),
            _uiState.map { it.tabSelectedIndex }.distinctUntilChanged()
        ) { query, tabIndex ->
            Pair(query, tabIndex)
        }.distinctUntilChanged()
            .flatMapLatest { (query, tabIndex) ->
                flow { emit(notesLocalRepository.getItemList()) }
            }.onEach { list ->
                println("NotesListViewModel list ${list.size}")
                _uiState.update { state ->
                    state.copy(
                        notesList = list.map { it.toNotesItem() }
                    )
                }
            }.flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

    }

    private fun onUiAction(action: NotesListUiAction) {
        when (action) {
            is NotesListUiAction.SearchNotes -> {
                _uiState.update {
                    it.copy(query = action.query)
                }
            }

            is NotesListUiAction.ChangeTab -> {
                _uiState.update {
                    it.copy(tabSelectedIndex = action.tabIndex)
                }
            }

            is NotesListUiAction.ChangeCategory -> {
                _uiState.update {
                    it.copy(category = action.category)
                }
            }
        }
    }

    private fun sendUiEvent(event: NotesListUiEvent) {
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

sealed interface NotesListUiModel {
    data class Item(val notesItem: List<NotesItem>) : NotesListUiModel
    data object Footer : NotesListUiModel
    data object Loading : NotesListUiModel
    data object PlaceHolder : NotesListUiModel
}


data class NotesListUiState(
    val loadStates: LoadStates = LoadStates.IDLE,
    val notesList: List<NotesItem> = listOf(),
    val favourites: List<NotesItem> = listOf(),
    val tabSelectedIndex: Int = 0,

    val query: String = "",
    val category: Category? = null,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
) {
    fun toAllUiModel(): List<NotesListUiModel> {
        return if (notesList.isEmpty()) {
            listOf(NotesListUiModel.PlaceHolder)
        } else {
           listOf(
               NotesListUiModel.Item(notesList)
           ).plus(
                    listOf(NotesListUiModel.Footer)
                )
        }
    }

    fun toFavouritesUiModel(): List<NotesListUiModel> {
        return if (favourites.isEmpty()) {
            listOf(NotesListUiModel.PlaceHolder)
        } else {
            listOf(
                NotesListUiModel.Item(favourites)
            ).plus(
                listOf(NotesListUiModel.Footer)
            )
        }
    }
}

sealed interface NotesListUiAction {
    data class ChangeTab(val tabIndex: Int) : NotesListUiAction
    data class SearchNotes(val query: String) : NotesListUiAction
    data class ChangeCategory(val category: Category) : NotesListUiAction
}

sealed interface NotesListUiEvent {

}