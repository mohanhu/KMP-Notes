package org.codeloop.notes.core.database

import androidx.room.RoomDatabase

expect class CommonDataBaseFactory {
    fun create() : RoomDatabase.Builder<CommonDataBase>
}