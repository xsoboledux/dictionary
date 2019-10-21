package ru.xsobolx.dictionary.data.repositories.translation

import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.data.prefs.base.PrefsStorage
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository.Companion.FROM_LANGUAGE_KEY
import ru.xsobolx.dictionary.data.repositories.translation.LanguageRepository.Companion.TO_LANGUAGE_KEY
import ru.xsobolx.dictionary.domain.translation.fromEnLanguageEntity
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.LanguageEntity
import ru.xsobolx.dictionary.domain.translation.model.TranslationDirection
import ru.xsobolx.dictionary.domain.translation.toRuLanguageEntity

class LanguageRepositoryTest {

    @Mock
    private lateinit var prefStorage: PrefsStorage
    private lateinit var languageRepository: LanguageRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        languageRepository = LanguageRepository.Impl(prefStorage)
    }

    @Test
    fun shouldSetLanguage() {
        val langEntity = fromEnLanguageEntity

        val testSubscriber = TestObserver<Unit>()
        val actual = languageRepository.setLanguage(langEntity)
        actual.subscribe(testSubscriber)

        testSubscriber.assertComplete()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun shouldGetFromLanguage() {
        val translationDirection = TranslationDirection.FROM
        `when`(prefStorage.getString(FROM_LANGUAGE_KEY)).thenReturn("EN")

        val testSubscriber = TestObserver<LanguageEntity>()
        val actual = languageRepository.getLanguage(translationDirection)
        actual.subscribe(testSubscriber)

        val expected = fromEnLanguageEntity
        testSubscriber.assertResult(expected)
    }

    @Test
    fun shouldGetToLanguage() {
        val translationDirection = TranslationDirection.TO
        `when`(prefStorage.getString(TO_LANGUAGE_KEY)).thenReturn("RU")

        val testSubscriber = TestObserver<LanguageEntity>()
        val actual = languageRepository.getLanguage(translationDirection)
        actual.subscribe(testSubscriber)

        val expected = toRuLanguageEntity
        testSubscriber.assertResult(expected)
    }

    @Test
    fun shouldReturnLanguages() {
        val testSubscriber = TestObserver<Set<Language>>()
        languageRepository.getLanguages()
            .subscribe(testSubscriber)

        val expected = setOf(Language.RU, Language.EN, Language.DE, Language.ES)
        testSubscriber.assertResult(expected)
    }
}