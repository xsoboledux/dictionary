package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

class GetAllSavedTranslationUseCase
    @Inject constructor(
        private val translationRepository: TranslationRepository
    ): UseCase<Any?, List<DictionaryEntry>> {
    override fun execute(parameter: Any?): Single<List<DictionaryEntry>> {
        return translationRepository.getAllTranslations()
            .subscribeOn(Schedulers.io())
    }
}