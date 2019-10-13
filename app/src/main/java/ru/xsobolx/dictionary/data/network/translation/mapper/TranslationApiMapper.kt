package ru.xsobolx.dictionary.data.network.translation.mapper

import ru.xsobolx.dictionary.data.Mapper
import ru.xsobolx.dictionary.data.network.translation.TranslationResponse
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import javax.inject.Inject

interface TranslationApiMapper : Mapper<TranslationResponse, DictionaryEntry> {
    class Impl @Inject constructor() : TranslationApiMapper {
        override fun map(value: TranslationResponse): DictionaryEntry {
            return DictionaryEntry(
                word = "",
                translation = value.text.toString(),
                language = mapLanguage(value.lang)
            )
        }

        private fun mapLanguage(lang: String): Language {
            return lang
                .split('-')
                .filter { it != Language.RU.lang }
                .map { Language.valueOf(it) }
                .first()
        }
    }
}


