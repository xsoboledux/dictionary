package ru.xsobolx.dictionary.data.database

import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.data.network.translation.TranslationResponse
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord

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
val testDictionaryDataBaseModel1 = DictionaryDBModel(
    word = "test1",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    translation = "тест",
    isFavorite = true
)
val testDictionaryDataBaseModel2 = DictionaryDBModel(
    word = "test2",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    translation = "тест",
    isFavorite = true
)
val testDictionaryDataBaseModel3 = DictionaryDBModel(
    word = "test3",
    fromLanguage = Language.EN,
    toLanguage = Language.RU,
    translation = "тест",
    isFavorite = true
)