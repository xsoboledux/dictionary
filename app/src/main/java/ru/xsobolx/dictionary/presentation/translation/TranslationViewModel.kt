package ru.xsobolx.dictionary.presentation.translation

import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

sealed class TranslationViewModel {
    object Loading : TranslationViewModel()
    data class Failure(val throwable: Throwable) : TranslationViewModel()
    data class Success(val dictionaryEntry: DictionaryEntry) : TranslationViewModel()
}