package ru.xsobolx.dictionary.data.db.translation.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import ru.xsobolx.dictionary.domain.translation.model.Language

const val DICTIONARY_ENTRIES_TABLE_NAME = "DictionaryEntries"

@Fts4
@Entity(tableName = DICTIONARY_ENTRIES_TABLE_NAME)
data class DictionaryDBModel(
    val word: String,
    val fromLanguage: Language,
    val toLanguage: Language,
    val translation: String,
    val isFavorite: Boolean
)