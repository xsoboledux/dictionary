package ru.xsobolx.dictionary.presentation.translation.view

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.presentation.base.BaseMvpView
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslateScreenLanguagedViewModel

interface TranslationView : BaseMvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTranslation(dictionaryEntry: DictionaryEntry)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFromLanguage(language: Language)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setToLanguage(language: Language)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLanguages(viewModel: TranslateScreenLanguagedViewModel)
}