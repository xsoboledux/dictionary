package ru.xsobolx.dictionary.app

import android.app.Application
import ru.xsobolx.dictionary.di.app.AppComponent
import ru.xsobolx.dictionary.di.app.AppModule

class DictionaryApp : Application() {
    private var appComponent: AppComponent? = null

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}