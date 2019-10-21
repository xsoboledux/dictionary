package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository
import ru.xsobolx.dictionary.domain.translation.model.LanguageEntity

class SetLanguageUseCaseTest {

    @Mock
    private lateinit var languageRepository: LanguageRepository
    private lateinit var setLanguageUseCase: SetLanguageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setLanguageUseCase = SetLanguageUseCase.Impl(languageRepository)
    }

    @Test
    fun shouldSetLanguageAndReturnThisLanguage() {
        val actualLanguageEntity = fromEnLanguageEntity
        `when`(languageRepository.setLanguage(actualLanguageEntity)).thenReturn(Completable.complete())

        val testSubscriber = TestObserver<LanguageEntity>()
        val actual = setLanguageUseCase.execute(actualLanguageEntity)
        actual.subscribe(testSubscriber)

        verify(languageRepository, times(1)).setLanguage(actualLanguageEntity)
        val expected = fromEnLanguageEntity.copy()
        testSubscriber.assertResult(expected)
    }
}
