package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language

class TranslateInteractorTest {
    lateinit var translateInteractor: TranslateInteractor
    @Mock
    private lateinit var translationRepository: TranslationRepository
    private lateinit var testSubscriber: TestObserver<DictionaryEntry>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testSubscriber = TestObserver()
        translateInteractor = TranslateInteractor(translationRepository)
    }

    @Test
    fun shouldReturnTranslatedWord() {
        val testEntry = DictionaryEntry(
            keyValue = "test",
            translation = "тест",
            language = Language.EN,
            isFavorite = false
        )
        `when`(translationRepository.getTranslation("тест")).thenReturn(Single.just(testEntry))

        val actual = translateInteractor.execute("тест")
        actual.subscribe(testSubscriber)

        verify(translationRepository, times(1)).getTranslation("тест")
        verifyNoMoreInteractions(translationRepository)

        testSubscriber.assertComplete()
    }
}