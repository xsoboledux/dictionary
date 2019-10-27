package ru.xsobolx.dictionary.data.db.translation.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language

class DictionaryDataBaseToDomainModelMapperTest {

    @Test
    fun shouldMapDataBaseToDomainModel() {
        val actual = DictionaryDBModel(
            word = "test",
            translation = "тест",
            fromLanguage = Language.EN,
            toLanguage = Language.RU,
            isFavorite = false
        )
        val expected = DictionaryEntry(
            word = "test",
            translation = "тест",
            fromLanguage = Language.EN,
            toLanguage = Language.RU,
            isFavorite = false
        )
        val mapper = DictionaryDataBaseToDomainModelMapper.Impl()

        assertEquals(mapper.map(actual), expected)
    }
}