package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.repositories.translation.TranslationRepository

class MakeTranslationFavoriteUseCaseTest {
    @Mock
    private lateinit var translationRepository: TranslationRepository
    private lateinit var makeTranslationFavoriteUseCase: MakeTranslationFavoriteUseCase
    private lateinit var testSubscriber: TestObserver<Unit>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testSubscriber = TestObserver()
        makeTranslationFavoriteUseCase = MakeTranslationFavoriteUseCase.Impl(translationRepository)
    }

    @Test
    fun shouldMakeTranslationFavorite() {
        `when`(translationRepository.updateTranslation(testEntry)).thenReturn(Completable.complete())

        val actual = makeTranslationFavoriteUseCase.execute(testEntry)
        actual.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertResult(Unit)
    }
}