package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

interface MakeTranslationFavoriteUseCase : UseCase<DictionaryEntry, Unit> {

    class Impl
    @Inject constructor(
        private val translationRepository: TranslationRepository
    ) : MakeTranslationFavoriteUseCase {
        override fun execute(parameter: DictionaryEntry): Single<Unit> {
            return translationRepository.updateTranslation(parameter)
                .toSingle { Unit }
                .subscribeOn(Schedulers.io())
        }
    }
}