package ru.xsobolx.dictionary.di.app

import dagger.Component
import retrofit2.Retrofit
import ru.xsobolx.dictionary.data.db.base.AppDatabase

@Component(modules = [AppModule::class, DataBaseModule::class, NetworkModule::class])
interface AppComponent {

    fun dataBase(): AppDatabase

    fun retrofit(): Retrofit
}