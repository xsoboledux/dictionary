package ru.xsobolx.dictionary.di.translation

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.xsobolx.dictionary.data.db.base.AppDatabase
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.network.translation.TranslationApi
import ru.xsobolx.dictionary.data.prefs.base.PrefsStorage
import ru.xsobolx.dictionary.di.app.AppContext

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

    @Provides
    @TranslationScope
    fun providePrefsStorage(@AppContext context: Context): PrefsStorage {
        return PrefsStorage.Impl(context)
    }
}