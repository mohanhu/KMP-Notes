package org.codeloop.notes.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual object CommonDataBaseConstructor :
    RoomDatabaseConstructor<RoomDatabase> {
    actual override fun initialize(): RoomDatabase {
        TODO("Not yet implemented")
    }
}

actual class CommonDataBaseFactory {
    actual fun create(): RoomDatabase.Builder<CommonDataBase> {
        val db = documentDirectory() + "/${CommonDataBase.DATABASE_NAME}"
        return Room.databaseBuilder(db)
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory() : String {
        val dir = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(dir).path.orEmpty()
    }
}

