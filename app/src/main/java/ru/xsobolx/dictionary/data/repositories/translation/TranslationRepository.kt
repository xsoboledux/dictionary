package ru.xsobolx.dictionary.data.repositories.translation

import io.reactivex.Single
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDataBaseModelMapper
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import javax.inject.Inject

interface TranslationRepository {

    fun search(value: String): Single<List<DictionaryEntry>>

    fun getAllTranslations(): Single<List<DictionaryEntry>>

    fun getTranslation(word: TranslatedWord): Single<DictionaryEntry>

    class Impl @Inject constructor(
        private val translationDAO: TranslationDAO,
        private val dictionaryDataBaseModelMapper: DictionaryDataBaseModelMapper
    ) : TranslationRepository {
        override fun search(value: String): Single<List<DictionaryEntry>> {
            return translationDAO.findTranslation(value)
                .map(dictionaryDataBaseModelMapper::map)
                .collect({ listOf<DictionaryEntry>() }, { l, p -> listOf(l, p) })
        }

        override fun getAllTranslations(): Single<List<DictionaryEntry>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getTranslation(word: TranslatedWord): Single<DictionaryEntry> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}

