package ru.xsobolx.dictionary.di.app

import dagger.Component
import retrofit2.Retrofit
import ru.xsobolx.dictionary.data.db.base.AppDatabase
import ru.xsobolx.dictionary.di.translation.TranslationComponent
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataBaseModule::class, NetworkModule::class])
@Singleton
interface AppComponent {

    fun translationComponentBuilder(): TranslationComponent.Builder
}