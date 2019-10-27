package ru.xsobolx.dictionary.di.presentation.favorites

import dagger.Subcomponent
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.favorites.ui.FavoritesFragment

@TranslationScreenScope
@Subcomponent
interface FavoritesScreenComponent {

    fun inject(fragment: FavoritesFragment)
}