package ru.xsobolx.dictionary.data.repositories.translation

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDataBaseToDomainModelMapper
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDomainToDataBaseModelMapper
import ru.xsobolx.dictionary.data.network.translation.TranslationApi
import ru.xsobolx.dictionary.data.network.translation.mapper.TranslationApiMapper
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import javax.inject.Inject

interface TranslationRepository {

    fun search(value: String): Maybe<List<DictionaryEntry>>

    fun getAllSavedTranslations(): Single<List<DictionaryEntry>>

    fun getTranslation(word: TranslatedWord): Single<DictionaryEntry>

    fun updateTranslation(entry: DictionaryEntry): Completable

    class Impl
    @Inject constructor(
        private val translationDAO: TranslationDAO,
        private val dictionaryDataBaseToDomainModelMapper: DictionaryDataBaseToDomainModelMapper,
        private val dictionaryDomainToDataBaseModelMapper: DictionaryDomainToDataBaseModelMapper,
        private val translationApi: TranslationApi,
        private val translationApiMapper: TranslationApiMapper
    ) : TranslationRepository {
        override fun updateTranslation(entry: DictionaryEntry): Completable {
           return translationDAO.updateDictionaryEntry(entry.word, entry.isFavorite)
        }

        override fun search(value: String): Maybe<List<DictionaryEntry>> {
            return translationDAO.searchTranslation(value)
                .map { it.map(dictionaryDataBaseToDomainModelMapper::map) }
        }

        override fun getAllSavedTranslations(): Single<List<DictionaryEntry>> {
            return translationDAO.loadAllDictionaryEntries()
                .map { it.map(dictionaryDataBaseToDomainModelMapper::map) }
        }

        override fun getTranslation(word: TranslatedWord): Single<DictionaryEntry> {
            val lang = "${word.fromLanguage.lang}-${word.toLanguage.lang}"
            return translationApi.translate(lang = lang, text = word.word)
                .retry(3)
                .map { response -> word.word to response }
                .map(translationApiMapper::map)
                .doOnSuccess { dictionaryEntry ->
                    val dbModel = dictionaryDomainToDataBaseModelMapper.map(dictionaryEntry)
                    translationDAO.insertDictionaryEntry(dbModel)
                }
        }
    }
}

