package ru.xsobolx.dictionary.domain.translation

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.RxRule
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.LanguageEntity
import ru.xsobolx.dictionary.domain.translation.model.TranslationDirection


class GetLanguageUseCaseTest {

    @Mock
    private lateinit var languageRepository: LanguageRepository
    private lateinit var getLanguageUseCase: GetLanguageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getLanguageUseCase = GetLanguageUseCase.Impl(languageRepository)
    }

    @Test
    fun shouldReturnFromLanguageEntity() {
        val translationDirection = TranslationDirection.FROM
        val actualLanguageEntity = fromEnLanguageEntity
        `when`(languageRepository.getLanguage(translationDirection)).thenReturn(
            Single.just(
                actualLanguageEntity
            )
        )

        val testSubscriber = TestObserver<LanguageEntity>()
        val actual = getLanguageUseCase.execute(translationDirection)
        actual.subscribe(testSubscriber)

        val expected = LanguageEntity(
            translationDirection = TranslationDirection.FROM,
            language = Language.EN
        )
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertResult(expected)
    }

    @Test
    fun shouldReturnToLanguageEntity() {
        val translationDirection = TranslationDirection.TO
        val actualLanguageEntity = toRuLanguageEntity
        `when`(languageRepository.getLanguage(translationDirection)).thenReturn(
            Single.just(
                actualLanguageEntity
            )
        )

        val testSubscriber = TestObserver<LanguageEntity>()
        val actual = getLanguageUseCase.execute(translationDirection)
        actual.subscribe(testSubscriber)

        val expected = LanguageEntity(
            translationDirection = TranslationDirection.TO,
            language = Language.RU
        )
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertResult(expected)
    }
}