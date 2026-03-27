package org.codeloop.notes.features.notes.data.repository

import kotlinx.coroutines.flow.Flow
import org.codeloop.notes.core.database.CommonDataBase
import org.codeloop.notes.features.notes.data.local.model.NoteItemEntity
import org.codeloop.notes.features.notes.domain.repository.NotesLocalRepository

class NotesLocalRepositoryImpl(
    private val commonDataBase: CommonDataBase
) : NotesLocalRepository {
    override suspend fun insertItem(notesItemEntity: NoteItemEntity) {
        commonDataBase.notesItemDao.insertItem(notesItemEntity)
    }

    override suspend fun insertItems(notesItemEntity: List<NoteItemEntity>) {
        commonDataBase .notesItemDao.insertItems(notesItemEntity)
    }

    override fun getItemList(): List<NoteItemEntity> {
        return commonDataBase.notesItemDao.getItems()
    }

    override fun getItemList(
        category: String?,
        search: String?,
        startDate: Long?,
        endDate: Long?
    ): Flow<List<NoteItemEntity>> {
        return commonDataBase.notesItemDao.getItemList(
            category = category,
            search = search,
            startDate = startDate,
            endDate = endDate
        )
    }

    override fun updateFavorite(id: Int, isFavorite: Boolean) {
        commonDataBase.notesItemDao.updateFavorite(id, isFavorite)
    }

    override fun getItemById(id: Int): Flow<NoteItemEntity?> {
        return commonDataBase.notesItemDao.getItemById(id)
    }

    override suspend fun deleteItemById(id: Int) {
        commonDataBase.notesItemDao.deleteItemById(id)
    }

    override suspend fun deleteAll() {
        commonDataBase.notesItemDao.deleteAll()
    }

}