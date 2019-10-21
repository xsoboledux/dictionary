package ru.xsobolx.dictionary.di.translation

import dagger.Subcomponent
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.network.translation.TranslationApi
import ru.xsobolx.dictionary.di.presentation.translate.TranslationScreenComponent
import ru.xsobolx.dictionary.domain.translation.*

@TranslationScope
@Subcomponent(modules = [TranslationDataSourceModule::class, TranslationDomainModule::class])
interface TranslationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): TranslationComponent
    }

    fun translateUseCase(): TranslateUseCase

    fun getAllSavedTranslationUseCase(): GetAllSavedTranslationUseCase

    fun searchTranslationUseCase(): SearchTranslationUseCase

    fun getLanguageUseCase(): GetLanguageUseCase

    fun setLanguageUseCase(): SetLanguageUseCase

    fun getAllLanguagesUseCase(): GetAllLanguagesUseCase

    fun translationScreenComponentBuilder(): TranslationScreenComponent.Builder
}