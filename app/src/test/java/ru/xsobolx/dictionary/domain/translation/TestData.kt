package ru.xsobolx.dictionary.domain.translation

import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.data.network.translation.TranslationResponse
import ru.xsobolx.dictionary.domain.translation.model.*

val testEntry = DictionaryEntry(
    word = "test",
    translation = "тест",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    isFavorite = false
)

val testTranslatedWord = TranslatedWord(
    word = "test",
    fromLanguage = Language.EN,
    toLanguage = Language.RU
)

val testTranslationResponse = TranslationResponse(
    code = 200,
    lang = "en-ru",
    text = listOf("тест")
)

val testDictionaryDataBaseModel = DictionaryDBModel(
    word = "test",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    translation = "тест",
    isFavorite = true
)

val testFromLanguageEntity = LanguageEntity(
    translationDirection = TranslationDirection.FROM,
    language = Language.EN
)

val testToLanguageEntity = LanguageEntity(
    translationDirection = TranslationDirection.TO,
    language = Language.RU
)

val testLanguagesSet = setOf(Language.DE, Language.EN, Language.ES, Language.RU)

val testFromLanguage = LanguageEntity(Language.EN, TranslationDirection.FROM)

val testToLanguage = LanguageEntity(Language.RU, TranslationDirection.TO)
