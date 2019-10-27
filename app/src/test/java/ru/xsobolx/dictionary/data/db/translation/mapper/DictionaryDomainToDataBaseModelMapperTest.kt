package ru.xsobolx.dictionary.data.db.translation.mapper

import org.junit.Assert.*
import org.junit.Test
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language

class DictionaryDomainToDataBaseModelMapperTest {

    @Test
    fun shouldMapDomainToDatabaseModel() {
        val actual = DictionaryEntry(
            word = "test",
            translation = "тест",
            fromLanguage = Language.EN,
            toLanguage = Language.RU,
            isFavorite = false
        )
        val expected = DictionaryDBModel(
            word = "test",
            translation = "тест",
            fromLanguage = Language.EN,
            toLanguage = Language.RU,
            isFavorite = false
        )
        val mapper = DictionaryDomainToDataBaseModelMapper.Impl()

        assertEquals(mapper.map(actual), expected)
    }
}