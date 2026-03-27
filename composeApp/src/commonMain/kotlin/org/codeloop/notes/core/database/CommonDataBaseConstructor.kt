package org.codeloop.notes.core.database

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

expect object CommonDataBaseConstructor : RoomDatabaseConstructor<RoomDatabase> {
    override fun initialize(): RoomDatabase
}