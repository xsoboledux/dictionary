package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Maybe
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

class SearchTranslationUseCaseTest {
    @Mock
    private lateinit var tranlsationRepository: TranslationRepository
    private lateinit var searchTranslationUseCase: SearchTranslationUseCase
    private lateinit var testSubscriber: TestObserver<DictionaryEntry>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testSubscriber = TestObserver()
        searchTranslationUseCase = SearchTranslationUseCase.Impl(tranlsationRepository)
    }

    @Test
    fun shouldSearchDictionaryEntry() {
        `when`(tranlsationRepository.search("test")).thenReturn(Maybe.just(testEntry))

        val actual = searchTranslationUseCase.execute("test")
        actual.subscribe(testSubscriber)

        verify(tranlsationRepository, times(1)).search("test")
        verifyNoMoreInteractions(tranlsationRepository)

        val expected = testEntry
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertResult(expected)
    }
}