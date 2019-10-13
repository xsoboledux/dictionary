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

class TranslateUseCaseTest {
    lateinit var translateUseCase: TranslateUseCase
    @Mock
    private lateinit var translationRepository: TranslationRepository
    private lateinit var testSubscriber: TestObserver<DictionaryEntry>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testSubscriber = TestObserver()
        translateUseCase = TranslateUseCase(translationRepository)
    }

    @Test
    fun shouldReturnDictionaryEntry() {
        `when`(translationRepository.getTranslation(testTranslatedWord)).thenReturn(Single.just(testEntry))

        val actual = translateUseCase.execute(testTranslatedWord)
        actual.subscribe(testSubscriber)

        verify(translationRepository, times(1)).getTranslation(testTranslatedWord)
        verifyNoMoreInteractions(translationRepository)

        testSubscriber.assertComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertResult(testEntry)
    }
}