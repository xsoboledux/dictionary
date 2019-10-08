package ru.xsobolx.dictionary.domain.translation.model

data class DictionaryEntry (
    val keyValue: String,
    val translation: String,
    val language: Language,
    val isFavorite: Boolean
)

enum class Language {
    EN, DE, ES
}