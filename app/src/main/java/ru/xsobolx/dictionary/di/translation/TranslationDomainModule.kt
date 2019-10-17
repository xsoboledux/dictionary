package ru.xsobolx.dictionary.di.translation

import dagger.Binds
import dagger.Module
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDataBaseToDomainModelMapper
import ru.xsobolx.dictionary.data.db.translation.mapper.DictionaryDomainToDataBaseModelMapper
import ru.xsobolx.dictionary.data.network.translation.mapper.TranslationApiMapper
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.SearchTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.TranslateUseCase

@Module
interface TranslationDomainModule {

    @TranslationScope
    @Binds
    fun provideTranslateUseCase(useCase: TranslateUseCase.Impl): TranslateUseCase

    @TranslationScope
    @Binds
    fun provideSearchUseCase(useCase: SearchTranslationUseCase.Impl): SearchTranslationUseCase

    @TranslationScope
    @Binds
    fun provideGetAllTranslationsUseCase(useCase: GetAllSavedTranslationUseCase.Impl): GetAllSavedTranslationUseCase

    @TranslationScope
    @Binds
    fun provideTranslationRepository(repository: TranslationRepository.Impl): TranslationRepository

    @TranslationScope
    @Binds
    fun provideDomainMapper(mapper: DictionaryDomainToDataBaseModelMapper.Impl): DictionaryDomainToDataBaseModelMapper

    @TranslationScope
    @Binds
    fun provideDataBaseMapper(mapper: DictionaryDataBaseToDomainModelMapper.Impl): DictionaryDataBaseToDomainModelMapper

    @TranslationScope
    @Binds
    fun provideApiMapper(mapper: TranslationApiMapper.Impl): TranslationApiMapper
}