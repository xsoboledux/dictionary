package ru.xsobolx.dictionary.data.db.translation.mapper

import ru.xsobolx.dictionary.data.Mapper
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import javax.inject.Inject

interface DictionaryDataBaseToDomainModelMapper : Mapper<DictionaryDBModel, DictionaryEntry> {

    class Impl @Inject constructor() : DictionaryDataBaseToDomainModelMapper {
        override fun map(value: DictionaryDBModel): DictionaryEntry {
            return DictionaryEntry(
                word = value.word,
                translation = value.translation,
                fromLanguage = value.fromLanguage,
                toLanguage = value.toLanguage,
                isFavorite = value.isFavorite
            )
        }
    }
}

