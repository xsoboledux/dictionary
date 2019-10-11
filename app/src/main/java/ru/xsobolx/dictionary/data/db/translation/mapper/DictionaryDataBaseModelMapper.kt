package ru.xsobolx.dictionary.data.db.translation.mapper

import ru.xsobolx.dictionary.data.Mapper
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import javax.inject.Inject

class DictionaryDataBaseModelMapper
@Inject constructor() : Mapper<DictionaryDBModel, DictionaryEntry> {
    override fun map(value: DictionaryDBModel): DictionaryEntry {
        return DictionaryEntry(
            word = value.word,
            translation = value.translation.toString(),
            language = Language.valueOf(value = value.language),
            isFavorite = value.isFavorite
        )
    }
}