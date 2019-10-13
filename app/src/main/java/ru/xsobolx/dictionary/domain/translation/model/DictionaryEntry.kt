package ru.xsobolx.dictionary.domain.translation.model

data class DictionaryEntry (
    val word: String,
    val translation: String,
    val language: Language,
    val isFavorite: Boolean = false
)
