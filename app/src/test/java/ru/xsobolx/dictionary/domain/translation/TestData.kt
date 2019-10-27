package ru.xsobolx.dictionary.domain.translation

import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.data.network.translation.TranslationResponse
import ru.xsobolx.dictionary.domain.translation.model.*
import ru.xsobolx.dictionary.presentation.translation.presenter.LanguagesViewModel

val testEntry = DictionaryEntry(
    word = "test",
    translation = "тест",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    isFavorite = false
)

val testEntry2 = DictionaryEntry(
    word = "find",
    translation = "найти",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    isFavorite = false
)

val testEntry3 = DictionaryEntry(
    word = "house",
    translation = "дом",
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
    isFavorite = false
)

val testLanguagesSet = setOf(Language.DE, Language.EN, Language.ES, Language.RU)

val fromEnLanguageEntity = LanguageEntity(Language.EN, TranslationDirection.FROM)
val fromRuLanguageEntity = LanguageEntity(Language.RU, TranslationDirection.FROM)

val toRuLanguageEntity = LanguageEntity(Language.RU, TranslationDirection.TO)
val toEnLanguageEntity = LanguageEntity(Language.EN, TranslationDirection.TO)

val fromENToRULanguageModel = LanguagesViewModel(
    allLanguages = testLanguagesSet,
    fromLanguage = Language.EN,
    toLanguage = Language.RU
)

val fromRUToEnLanguageModel = LanguagesViewModel(
    allLanguages = testLanguagesSet,
    fromLanguage = Language.RU,
    toLanguage = Language.EN
)

