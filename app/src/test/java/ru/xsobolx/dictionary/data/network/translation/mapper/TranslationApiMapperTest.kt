package ru.xsobolx.dictionary.data.network.translation.mapper

import org.junit.Assert.*
import org.junit.Test
import ru.xsobolx.dictionary.data.network.translation.TranslationResponse
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language

class TranslationApiMapperTest {

    @Test
    fun shouldMapApiResponseToDomain() {
        val response = TranslationResponse(
            code = 200,
            lang = "en-ru",
            text = listOf("тест", "испытание", "проверка")
        )
        val expected = DictionaryEntry(
            word = "test",
            translation = "тест, испытание, проверка",
            fromLanguage = Language.EN,
            toLanguage = Language.RU,
            isFavorite = false
        )

        val mapper = TranslationApiMapper.Impl()
        val actual = mapper.map("test" to response)

        assertEquals(actual, expected)
    }
}