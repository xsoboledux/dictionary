package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.Language
import javax.inject.Inject

interface GetAllLanguagesUseCase : UseCase<Unit, Set<Language>> {

    class Impl
    @Inject constructor(
        private val languageRepository: LanguageRepository
    ) : GetAllLanguagesUseCase {
        override fun execute(parameter: Unit): Single<Set<Language>> {
            return languageRepository.getLanguages()
                .subscribeOn(Schedulers.io())
        }
    }
}