package ru.xsobolx.dictionary.di.translation

import dagger.Binds
import dagger.Module
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslationPresenter

@Module
abstract class TranslationModule {

//    @Provides
//    @TranslationScope
//    fun provideTranslationApi(retrofit: Retrofit): TranslationApi {
//        return retrofit.create(TranslationApi::class.java)
//    }
//
//    @Provides
//    @TranslationScope
//    fun provideTranslationDao(database: AppDatabase): TranslationDAO {
//        return database.translationDao()
//    }

    @TranslationScope
    @Binds
    abstract fun provideTranslationPresenter(translationPresenter: TranslationPresenter): TranslationPresenter

}