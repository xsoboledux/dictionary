package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository
import ru.xsobolx.dictionary.domain.translation.model.Language

class GetAllLanguagesUseCaseTest {

    @Mock
    private lateinit var languageRepository: LanguageRepository
    private lateinit var getAllLanguagesUseCase: GetAllLanguagesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getAllLanguagesUseCase = GetAllLanguagesUseCase.Impl(languageRepository)
    }

    @Test
    fun shouldReturnLanguages() {
        val actual = setOf(Language.ES, Language.DE, Language.EN, Language.RU)
        `when`(languageRepository.getLanguages()).thenReturn(Single.just(actual))

        val testSubscriber = TestObserver<Set<Language>>()
        getAllLanguagesUseCase.execute(Unit)
            .subscribe(testSubscriber)

        verify(languageRepository, times(1)).getLanguages()
        val expected = setOf(Language.ES, Language.DE, Language.EN, Language.RU)
        testSubscriber.assertResult(expected)
    }
}