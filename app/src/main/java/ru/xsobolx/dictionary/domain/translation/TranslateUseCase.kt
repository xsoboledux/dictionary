package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import javax.inject.Inject

interface TranslateUseCase : UseCase<TranslatedWord, DictionaryEntry> {

    class Impl @Inject constructor(
        private val translationRepository: TranslationRepository
    ) : TranslateUseCase {
        override fun execute(parameter: TranslatedWord): Single<DictionaryEntry> {
            return translationRepository.getTranslation(parameter)
        }
    }
}