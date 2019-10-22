package ru.xsobolx.dictionary.di.presentation.translate

import dagger.Subcomponent
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslationPresenter
import ru.xsobolx.dictionary.presentation.translation.ui.TranslationFragment

@TranslationScreenScope
@Subcomponent
interface TranslationScreenComponent {

    fun translationPresenter(): TranslationPresenter

    fun inject(fragment: TranslationFragment)
}