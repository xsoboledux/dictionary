package ru.xsobolx.dictionary.di.presentation.translate

import dagger.Subcomponent
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslationPresenter

@TranslationScreenScope
@Subcomponent(modules = [TranslateScreenModule::class])
interface TranslationScreenComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): TranslationScreenComponent
    }

    fun translationPresenter(): TranslationPresenter
}