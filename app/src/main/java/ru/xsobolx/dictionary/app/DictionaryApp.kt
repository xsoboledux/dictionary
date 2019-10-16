package ru.xsobolx.dictionary.app

import android.app.Application
import ru.xsobolx.dictionary.di.app.AppComponent
import ru.xsobolx.dictionary.di.app.AppModule
import ru.xsobolx.dictionary.di.app.DaggerAppComponent

class DictionaryApp : Application() {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}