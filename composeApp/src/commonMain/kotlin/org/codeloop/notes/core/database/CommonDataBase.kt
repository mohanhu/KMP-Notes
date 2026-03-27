package org.codeloop.notes.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.codeloop.notes.features.notes.data.local.dao.NotesItemDao
import org.codeloop.notes.features.notes.data.local.model.NoteItemEntity

@Database(
    entities = [
        NoteItemEntity::class
    ],
    version = 2,
    exportSchema = false,
)
abstract class CommonDataBase : RoomDatabase(){

    abstract val notesItemDao : NotesItemDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}