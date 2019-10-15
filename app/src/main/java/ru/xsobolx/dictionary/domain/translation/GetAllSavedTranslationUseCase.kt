package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

interface GetAllSavedTranslationUseCase : UseCase<Any?, List<DictionaryEntry>> {

    class Impl @Inject
    constructor(
        private val translationRepository: TranslationRepository
    ) : GetAllSavedTranslationUseCase {
        override fun execute(parameter: Any?): Single<List<DictionaryEntry>> {
            return translationRepository.getAllSavedTranslations()
                .subscribeOn(Schedulers.io())
        }
    }
}