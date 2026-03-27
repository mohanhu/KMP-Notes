package org.codeloop.notes.features.notes.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeloop.notes.features.notes.domain.model.Category
import org.codeloop.notes.features.notes.domain.model.NotesItem
import org.codeloop.notes.features.notes.domain.model.toEntity
import org.codeloop.notes.features.notes.domain.model.toNotesItem
import org.codeloop.notes.features.notes.domain.repository.NotesLocalRepository
import org.codeloop.notes.utils.loadstate.LoadState
import org.codeloop.notes.utils.loadstate.LoadStates
import org.codeloop.notes.utils.loadstate.LoadType
import kotlin.time.Clock

class NotesPreviewViewModel (
    private val notesLocalRepository: NotesLocalRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val notesId = savedStateHandle.getStateFlow<Int>("notesId",-1)

    private val _uiState = MutableStateFlow(NotesPreviewUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<NotesPreviewUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept : (NotesPreviewUiAction) -> Unit = ::onUiAction

    private var deleteJob : Job ? = null

    init {

        notesId
            .filterNotNull()
            .filterNot { it < 0 }
            .debounce(300)
            .flatMapLatest { notesId ->
                notesLocalRepository.getItemById(notesId)
            }.filterNotNull()
            .onEach { notes ->
                println("NotesPreviewViewModel notes ${notesId.value} : ${notes.toNotesItem()}")
                _uiState.update { state ->
                    state.copy(
                        notesList = notes.toNotesItem(),
                        title = notes.title,
                        description = notes.description,
                        category = Category.entries.firstOrNull{it.name == notes.category}?: Category.GENERAL
                    )
                }
            }.flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun onUiAction(action : NotesPreviewUiAction) {
        when(action) {
            is NotesPreviewUiAction.ChangeCategory -> {
                _uiState.update {
                    it.copy(category = action.category)
                }
            }
            is NotesPreviewUiAction.OnTypeDescription -> {
                _uiState.update {
                    it.copy(description = action.description)
                }
            }
            is NotesPreviewUiAction.OnTypeTitle -> {
                _uiState.update {
                    it.copy(title = action.title)
                }
            }
            NotesPreviewUiAction.SaveNote -> saveNote()
            NotesPreviewUiAction.DeleteNote -> deleteNote()
            NotesPreviewUiAction.FavoriteNote -> favoriteNote()
        }
    }

    private fun favoriteNote() {
        viewModelScope.launch(Dispatchers.IO) {
            notesLocalRepository.updateFavorite(
                id = _uiState.value.notesList?.id?:0,
                isFavorite = !(_uiState.value.notesList?.isFavourite?:false)
            )
        }
    }

    private fun saveNote() {
        viewModelScope.launch {

            val title = _uiState.value.title?:""
            val description = _uiState.value.description?:""
            val category = _uiState.value.category

            if (title.isBlank() || description.isBlank()) {
                return@launch
            }

            val notesItem = (_uiState.value.notesList?.copy(
                title = title,
                description = description,
                category = category,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            ) ?: NotesItem.create(
                title = title,
                description = description,
                category = category,
            )).toEntity()

            setLoading(LoadType.ACTION, LoadState.Loading())
            notesLocalRepository.insertItem(notesItem)
            delay(1000) // ui loader
            setLoading(LoadType.ACTION, LoadState.NotLoading.Complete)
            _uiState.update {
                it.copy(
                    notesCreated = true
                )
            }
        }
    }
    private fun deleteNote() {
        viewModelScope.launch {
            setLoading(LoadType.ACTION, LoadState.Loading())
            _uiState.value.notesList?.id?.let {
                notesLocalRepository.deleteItemById(it)
            }
            delay(1000)
            setLoading(LoadType.ACTION, LoadState.NotLoading.Complete)
            _uiState.update {
                it.copy(
                    notesDeleted = true
                )
            }
        }
    }

    private fun sendUiEvent(event : NotesPreviewUiEvent) {
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


data class NotesPreviewUiState(
    val loadStates: LoadStates = LoadStates.IDLE,
    val notesList : NotesItem? = null,

    val notesDeleted : Boolean = false,
    val notesCreated : Boolean = false,

    val title : String? = null,
    val description : String? = null,
    val category : Category = Category.GENERAL,
    val updatedDate : Long = Clock.System.now().toEpochMilliseconds()
)

sealed interface NotesPreviewUiAction {
    data class OnTypeTitle(val title : String?) : NotesPreviewUiAction
    data class OnTypeDescription(val description : String?) : NotesPreviewUiAction
    data class ChangeCategory(val category : Category) : NotesPreviewUiAction
    data object FavoriteNote : NotesPreviewUiAction
    data object SaveNote : NotesPreviewUiAction
    data object DeleteNote : NotesPreviewUiAction
}

sealed interface NotesPreviewUiEvent {

}