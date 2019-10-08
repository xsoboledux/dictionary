package ru.xsobolx.dictionary.domain.translation

import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.base.UseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import javax.inject.Inject

class FindTranslationInteractor @Inject constructor(
    private val translationRepository: TranslationRepository
) : UseCase<String, DictionaryEntry> {

    override fun execute(parameter: String) = translationRepository.getTranslation(parameter)
}