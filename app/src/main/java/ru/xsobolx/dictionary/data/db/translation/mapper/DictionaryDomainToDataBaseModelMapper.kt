package ru.xsobolx.dictionary.data.db.translation.mapper

import ru.xsobolx.dictionary.data.Mapper
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

interface DictionaryDomainToDataBaseModelMapper : Mapper<DictionaryEntry, DictionaryDBModel> {

    class Impl @Inject constructor() : DictionaryDomainToDataBaseModelMapper {
        override fun map(value: DictionaryEntry): DictionaryDBModel {
            return DictionaryDBModel(
                word = value.word,
                translation = value.translation.split(','),
                language = value.language.lang,
                isFavorite = value.isFavorite
            )
        }
    }
}