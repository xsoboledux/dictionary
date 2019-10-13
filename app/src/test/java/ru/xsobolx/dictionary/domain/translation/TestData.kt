package ru.xsobolx.dictionary.domain.translation

import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.data.network.translation.TranslationResponse
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord

val testEntry = DictionaryEntry(
    word = "test",
    translation = "тест",
    language = Language.EN,
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
    language = "en",
    translation = listOf("тест"),
    isFavorite = true
)