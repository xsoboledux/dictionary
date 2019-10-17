package ru.xsobolx.dictionary.di.presentation.translate

import dagger.Binds
import dagger.Module
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslationPresenter

@Module
interface TranslateScreenModule {

    @TranslationScreenScope
    @Binds
    fun translationPresenter(translationPresenter: TranslationPresenter): TranslationPresenter
}