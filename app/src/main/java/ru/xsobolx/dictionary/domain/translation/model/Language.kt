package ru.xsobolx.dictionary.domain.translation.model

enum class Language(val lang: String) {
    EN("en"), DE("de"), ES("es"), RU("ru")
}

data class LanguageEntity(
    val language: Language,
    val translationDirection: TranslationDirection
)

enum class TranslationDirection {
    FROM, TO
}