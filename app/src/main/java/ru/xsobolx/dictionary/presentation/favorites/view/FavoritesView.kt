package ru.xsobolx.dictionary.presentation.favorites.view

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BaseMvpView

interface FavoritesView : BaseMvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showFavorites(entries: List<DictionaryEntry>)
}