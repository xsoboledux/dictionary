package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

interface GetAllSavedTranslationUseCase : UseCase<Unit, List<DictionaryEntry>> {

    class Impl @Inject
    constructor(
        private val translationRepository: TranslationRepository
    ) : GetAllSavedTranslationUseCase {
        override fun execute(parameter: Unit): Single<List<DictionaryEntry>> {
            return translationRepository.getAllSavedTranslations()
                .map { it.distinct() }
                .subscribeOn(Schedulers.io())
        }
    }
}