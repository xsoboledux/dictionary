package ru.xsobolx.dictionary.di.presentation.phrasebook

import dagger.Subcomponent
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope
import ru.xsobolx.dictionary.presentation.phrasebook.presenter.PhraseBookPresenter
import ru.xsobolx.dictionary.presentation.phrasebook.ui.PhrasebookFragment

@TranslationScreenScope
@Subcomponent
interface PhrasebookScreenComponent {

    fun inject(fragment: PhrasebookFragment)
}