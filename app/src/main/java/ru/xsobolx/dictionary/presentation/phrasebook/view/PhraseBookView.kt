package ru.xsobolx.dictionary.presentation.phrasebook.view

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BaseMvpView

interface PhraseBookView : BaseMvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEntries(entries: List<DictionaryEntry>)
}