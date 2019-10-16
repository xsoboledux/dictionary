package ru.xsobolx.dictionary.di.translation

import dagger.Subcomponent

@TranslationScope
@Subcomponent(modules = [TranslationModule::class])
interface TranslationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): TranslationComponent
    }
}