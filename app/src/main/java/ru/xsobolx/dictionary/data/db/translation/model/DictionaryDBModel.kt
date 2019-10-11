package ru.xsobolx.dictionary.data.db.translation.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

const val DICTIONARY_ENTRIES_TABLE_NAME = "DictionaryEntries"

@Fts4
@Entity(tableName = DICTIONARY_ENTRIES_TABLE_NAME)
data class DictionaryDBModel(
    @PrimaryKey val word: String,
    val language: String,
    val translation: List<String>,
    val isFavorite: Boolean
)