package ru.xsobolx.dictionary.di.translation

import dagger.Component

@TranslationScope
@Component(modules = [TranslationModule::class])
interface TranslationComponent {

    @Component.Builder
    interface Builder {
        fun build(): TranslationComponent
    }
}