package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import javax.inject.Inject

class TranslateInteractor
@Inject constructor(
    private val translationRepository: TranslationRepository
): UseCase<TranslatedWord, DictionaryEntry> {
    override fun execute(parameter: TranslatedWord): Single<DictionaryEntry> {
        return translationRepository.getTranslation(parameter)
    }
}