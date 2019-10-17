package ru.xsobolx.dictionary.di.presentation.phrasebook

import dagger.Subcomponent
import ru.xsobolx.dictionary.di.presentation.TranslationScreenScope

@TranslationScreenScope
@Subcomponent(modules = [PhrasebookScreenModule::class])
interface PhrasebookScreenComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): PhrasebookScreenComponent
    }
}