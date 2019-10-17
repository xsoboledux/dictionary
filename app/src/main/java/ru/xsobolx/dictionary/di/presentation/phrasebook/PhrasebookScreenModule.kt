package ru.xsobolx.dictionary.di.presentation.phrasebook

import dagger.Binds
import dagger.Module
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.phrasebook.presenter.PhraseBookPresenter

@Module
interface PhrasebookScreenModule {

    @TranslationScreenScope
    @Binds
    fun phrasebookPresenter(presenter: PhraseBookPresenter): PhraseBookPresenter
}