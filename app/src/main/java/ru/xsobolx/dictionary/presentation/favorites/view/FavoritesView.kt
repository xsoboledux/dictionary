package ru.xsobolx.dictionary.presentation.favorites.view

import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BaseMvpView

interface FavoritesView : BaseMvpView {

    fun showFavorites(entries: List<DictionaryEntry>)
}