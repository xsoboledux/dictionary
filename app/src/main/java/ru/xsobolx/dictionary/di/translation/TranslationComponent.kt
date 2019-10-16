package ru.xsobolx.dictionary.di.translation

import dagger.Component
import ru.xsobolx.dictionary.di.app.AppComponent

@TranslationScope
@Component(modules = [TranslationModule::class], dependencies = [AppComponent::class])
interface TranslationComponent