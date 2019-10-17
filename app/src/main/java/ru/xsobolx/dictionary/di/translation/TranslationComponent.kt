package ru.xsobolx.dictionary.di.translation

import dagger.Subcomponent
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.network.translation.TranslationApi

@TranslationScope
@Subcomponent(modules = [TranslationModule::class])
interface TranslationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): TranslationComponent
    }

    fun translationApi(translationApi: TranslationApi): TranslationApi

    fun translationDAO(translationDAO: TranslationDAO): TranslationDAO
}