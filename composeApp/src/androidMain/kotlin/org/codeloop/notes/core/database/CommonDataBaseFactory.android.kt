package org.codeloop.notes.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

actual object CommonDataBaseConstructor :
    RoomDatabaseConstructor<RoomDatabase> {
    actual override fun initialize(): RoomDatabase {
        TODO("Not yet implemented")
    }
}

actual class CommonDataBaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<CommonDataBase> {
        val applicationContext = context.applicationContext
        return Room.databaseBuilder(
            context = applicationContext,
            name = applicationContext.getDatabasePath(CommonDataBase.DATABASE_NAME).absolutePath
        )
    }
}