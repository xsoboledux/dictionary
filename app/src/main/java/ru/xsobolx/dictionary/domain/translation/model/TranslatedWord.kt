package ru.xsobolx.dictionary.domain.translation.model

data class TranslatedWord(
    val word: String,
    val fromLanguage: Language,
    val toLanguage: Language
)