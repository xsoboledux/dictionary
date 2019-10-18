package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.LanguageEntity
import ru.xsobolx.dictionary.domain.translation.model.TranslationDirection
import javax.inject.Inject

interface GetLanguageUseCase : UseCase<TranslationDirection, LanguageEntity> {

    class Impl
    @Inject constructor(
        private val languageRepository: LanguageRepository
    ): GetLanguageUseCase {
        override fun execute(parameter: TranslationDirection): Single<LanguageEntity> {
            return languageRepository.getLanguage(parameter)
                .subscribeOn(Schedulers.io())
        }
    }
}