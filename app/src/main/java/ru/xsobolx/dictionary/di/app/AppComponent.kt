package ru.xsobolx.dictionary.di.app

import dagger.Component
import ru.xsobolx.dictionary.di.translation.TranslationComponent

@Component(modules = [AppModule::class, DataBaseModule::class, NetworkModule::class])
interface AppComponent {

    fun translationBuilder(): TranslationComponent.Builder
}