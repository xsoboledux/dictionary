package ru.xsobolx.dictionary.di.translation

import dagger.Subcomponent
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.network.translation.TranslationApi
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.SearchTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.TranslateUseCase

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
}