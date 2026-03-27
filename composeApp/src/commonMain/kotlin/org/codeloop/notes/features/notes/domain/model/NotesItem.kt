package org.codeloop.notes.features.notes.domain.model

import org.codeloop.notes.features.notes.data.local.model.NoteItemEntity
import kotlin.time.Clock

data class NotesItem (
    val id : Int = 0,
    val title : String,
    val description : String,
    val category : Category,
    val isFavourite : Boolean = false,
    val createdAt : Long,
    val updatedAt : Long
) {
    companion object {
        fun create(
            id : Int = 0,
            title : String,
            description : String,
            isFavourite : Boolean = false,
            category : Category,
            createdAt : Long = Clock.System.now().toEpochMilliseconds(),
            updatedAt : Long = Clock.System.now().toEpochMilliseconds()
        ): NotesItem {
            return NotesItem(
                id = id,
                title = title,
                description = description,
                category = category,
                isFavourite = isFavourite,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}

fun NoteItemEntity.toNotesItem() : NotesItem {
    return NotesItem(
        id = id,
        title = title,
        description = description,
        category = Category.entries.find { it.name == category } ?: Category.GENERAL,
        isFavourite = favourite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun NotesItem.toEntity() : NoteItemEntity {
    return NoteItemEntity(
        id = id,
        title = title,
        description = description,
        category = category.name,
        favourite = isFavourite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}