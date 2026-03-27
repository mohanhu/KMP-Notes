package org.codeloop.notes.features.notes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.codeloop.notes.features.notes.data.local.model.NoteItemEntity
import org.codeloop.notes.features.notes.data.local.model.NotesItemTable

@Dao
interface NotesItemDao {

    @Upsert
    suspend fun insertItem(notesItemEntity: NoteItemEntity)

    @Upsert
    suspend fun insertItems(notesItemEntity: List<NoteItemEntity>)

    @Query("""
        SELECT * FROM ${
        NotesItemTable.TABLE_NAME}
    """)
    fun getItems() : List<NoteItemEntity>

    @Query("""
        SELECT * FROM ${
        NotesItemTable.TABLE_NAME}
        WHERE ${NotesItemTable.Column.CATEGORY} = :category AND
        (${NotesItemTable.Column.TITLE} LIKE '%' || :search || '%' OR ${NotesItemTable.Column.DESCRIPTION} LIKE '%' || :search || '%') AND
        (${NotesItemTable.Column.CREATED_AT} BETWEEN :startDate AND :endDate)
        ORDER BY ${NotesItemTable.Column.CREATED_AT} DESC
    """)
    fun getItemList(
        category : String? = null,
        search : String? = null,
        startDate: Long? = null,
        endDate: Long? = null
    ) : Flow<List<NoteItemEntity>>

    @Query("""
        SELECT * FROM ${
        NotesItemTable.TABLE_NAME} WHERE ${NotesItemTable.Column.ID} = :id
        ORDER BY ${NotesItemTable.Column.CREATED_AT} DESC
    """)
    fun getItemById(id : Int) :  Flow<NoteItemEntity?>

    @Query("""
        UPDATE ${NotesItemTable.TABLE_NAME} SET ${NotesItemTable.Column.FAVOURITE} = :favorite WHERE ${NotesItemTable.Column.ID} = :id
    """)
    fun updateFavorite(id: Int, favorite: Boolean)

    @Query("""
        DELETE FROM ${
        NotesItemTable.TABLE_NAME} WHERE ${NotesItemTable.Column.ID} = :id
    """)
    suspend fun deleteItemById(id : Int)

    @Query("""
        DELETE FROM ${
        NotesItemTable.TABLE_NAME}
    """)
    suspend fun deleteAll()
}























