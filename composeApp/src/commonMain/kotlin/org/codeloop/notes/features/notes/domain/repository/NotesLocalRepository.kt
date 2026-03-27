package org.codeloop.notes.features.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import org.codeloop.notes.features.notes.data.local.model.NoteItemEntity

interface NotesLocalRepository {

    suspend fun insertItem(notesItemEntity: NoteItemEntity)
    suspend fun insertItems(notesItemEntity: List<NoteItemEntity>)
    fun getItemList() : List<NoteItemEntity>

    fun getItemList(
        category : String? = null,
        search : String? = null,
        startDate : Long? = null,
        endDate : Long?= null
    ) : Flow<List<NoteItemEntity>>

    fun getItemById(id : Int) : Flow<NoteItemEntity?>

    fun updateFavorite(id : Int, isFavorite : Boolean)

    suspend fun deleteItemById(id : Int)
    suspend fun deleteAll()


}