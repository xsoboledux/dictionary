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
                translation = value.text.joinToString(", "),
                fromLanguage = mapLanguage(value.lang).first,
                toLanguage = mapLanguage(value.lang).second
            )
        }

        private fun mapLanguage(lang: String): Pair<Language, Language> {
            val langs = lang .split('-', limit = 2)
                .map { Language.valueOf(it.toUpperCase()) }
            return langs[0] to langs[1]
        }
    }
}


