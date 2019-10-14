package ru.xsobolx.dictionary.presentation.translation.view

import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.presentation.base.BaseMvpView

interface TranslationView : BaseMvpView {

    fun showTranslation(dictionaryEntry: DictionaryEntry)

    fun setFromLanguage(language: Language)

    fun setToLanguage(language: Language)
}