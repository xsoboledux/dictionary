package ru.xsobolx.dictionary.app

import android.app.Application
import ru.xsobolx.dictionary.di.app.*

class DictionaryApp : Application() {
    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataBaseModule(DataBaseModule())
            .networkModule(NetworkModule())
            .build()
    }
}