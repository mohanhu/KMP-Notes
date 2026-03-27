package org.codeloop.notes.features.notes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = NotesItemTable.TABLE_NAME
)
data class NoteItemEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotesItemTable.Column.ID) val id : Int = 0,

    @ColumnInfo(name = NotesItemTable.Column.TITLE) val title : String,

    @ColumnInfo(name = NotesItemTable.Column.DESCRIPTION) val description : String,

    @ColumnInfo(name = NotesItemTable.Column.CATEGORY) val category : String,

    @ColumnInfo(name = NotesItemTable.Column.FAVOURITE) val favourite : Boolean,

    @ColumnInfo(name = NotesItemTable.Column.CREATED_AT) val createdAt : Long,

    @ColumnInfo(name = NotesItemTable.Column.UPDATED_AT) val updatedAt : Long
)

object NotesItemTable {
    const val TABLE_NAME = "notes"
    object Column {
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val CATEGORY = "category"

        const val FAVOURITE = "favourite"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
    }
}