package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

class GetAllSavedTranslationUseCaseTest {
    @Mock
    private lateinit var translationRepository: TranslationRepository
    private lateinit var getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase
    private lateinit var testSubscriber: TestObserver<List<DictionaryEntry>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testSubscriber = TestObserver()
        getAllSavedTranslationUseCase = GetAllSavedTranslationUseCase.Impl(translationRepository)
    }

    @Test
    fun shouldReturnListOfDictionaryEntries() {
        `when`(translationRepository.getAllSavedTranslations()).thenReturn(Single.just(listOf(testEntry)))

        val actual = getAllSavedTranslationUseCase.execute(Unit)
        actual.subscribe(testSubscriber)

        verify(translationRepository, times(1)).getAllSavedTranslations()
        verifyNoMoreInteractions(translationRepository)

        val expected = listOf(testEntry)
        testSubscriber.assertResult(expected)
    }
}