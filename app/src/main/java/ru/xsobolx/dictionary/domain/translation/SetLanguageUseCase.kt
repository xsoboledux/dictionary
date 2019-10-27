package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.LanguageEntity
import javax.inject.Inject

interface SetLanguageUseCase : UseCase<LanguageEntity, LanguageEntity> {

    class Impl
    @Inject constructor(
        private val languageRepository: LanguageRepository
    ): SetLanguageUseCase {
        override fun execute(parameter: LanguageEntity): Single<LanguageEntity> {
            return languageRepository.setLanguage(parameter)
                .toSingle { parameter }
                .subscribeOn(Schedulers.io())
        }
    }
}