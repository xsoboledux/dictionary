package ru.xsobolx.dictionary.di.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module(includes = [DataBaseModule::class])
class AppModule(private val context: Application) {

    @Provides
    @AppContext
    fun provideAppContext(): Context = context
}