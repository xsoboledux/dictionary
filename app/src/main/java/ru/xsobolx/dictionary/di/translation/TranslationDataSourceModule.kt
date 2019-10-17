package ru.xsobolx.dictionary.di.translation

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.xsobolx.dictionary.data.db.base.AppDatabase
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.network.translation.TranslationApi

@Module
class TranslationDataSourceModule {

    @Provides
    @TranslationScope
    fun provideTranslationApi(retrofit: Retrofit): TranslationApi {
        return retrofit.create(TranslationApi::class.java)
    }

    @Provides
    @TranslationScope
    fun provideTranslationDao(database: AppDatabase): TranslationDAO {
        return database.translationDao()
    }
}