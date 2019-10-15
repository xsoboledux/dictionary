package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

interface SearchTranslationUseCase : UseCase<String, DictionaryEntry> {
    class Impl
    @Inject
    constructor(
        private val translationRepository: TranslationRepository
    ) : SearchTranslationUseCase {

        override fun execute(parameter: String): Single<DictionaryEntry> {
            return translationRepository.search(parameter)
                .toSingle()
                .subscribeOn(Schedulers.io())
        }
    }
}