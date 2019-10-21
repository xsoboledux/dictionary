package ru.xsobolx.dictionary.di.presentation.phrasebook

import dagger.Subcomponent
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.phrasebook.presenter.PhraseBookPresenter
import ru.xsobolx.dictionary.presentation.phrasebook.ui.PhrasebookFragment

@TranslationScreenScope
@Subcomponent
interface PhrasebookScreenComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): PhrasebookScreenComponent
    }

    fun phrasebookPresenter(): PhraseBookPresenter

    fun inject(fragment: PhrasebookFragment)
}