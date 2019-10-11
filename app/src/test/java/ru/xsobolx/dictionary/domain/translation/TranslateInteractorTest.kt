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
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord

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
            word = "test",
            translation = "тест",
            language = Language.EN,
            isFavorite = false
        )
        val testTranslatedWord = TranslatedWord(
            word = "test",
            fromLanguage = Language.EN,
            toLanguage = Language.RU
        )
        `when`(translationRepository.getTranslation(testTranslatedWord)).thenReturn(Single.just(testEntry))

        val actual = translateInteractor.execute(testTranslatedWord)
        actual.subscribe(testSubscriber)

        verify(translationRepository, times(1)).getTranslation(testTranslatedWord)
        verifyNoMoreInteractions(translationRepository)

        testSubscriber.assertComplete()
    }
}