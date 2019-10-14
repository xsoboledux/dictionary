package ru.xsobolx.dictionary.presentation.translation.presenter

import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.domain.translation.TranslateUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import ru.xsobolx.dictionary.domain.translation.testEntry
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import ru.xsobolx.dictionary.presentation.translation.view.`TranslationView$$State`
import java.util.concurrent.TimeUnit

class TranslationPresenterTest {
    @Mock
    private lateinit var translationView: TranslationView
    @Mock
    private lateinit var translationViewState: `TranslationView$$State`
    private lateinit var uiScheduler: TestScheduler
    private lateinit var debounceScheduler: TestScheduler
    @Mock
    private lateinit var translateUseCase: TranslateUseCase
    private lateinit var presenter: TranslationPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        uiScheduler = TestScheduler()
        debounceScheduler = TestScheduler()
        presenter =
            TranslationPresenter(translateUseCase, uiScheduler = uiScheduler, debounceScheduler = debounceScheduler)
        presenter.setFromLanguage(Language.EN)
        presenter.setToLanguage(Language.RU)
        presenter.attachView(translationView)
        presenter.setViewState(translationViewState)
    }

    @Test
    fun shouldShowTranslationOnTextChanged() {
        val acualTranslatedWord = TranslatedWord(
            word = "test",
            fromLanguage = Language.EN,
            toLanguage = Language.RU
        )
        `when`(translateUseCase.execute(acualTranslatedWord)).thenReturn(Single.just(testEntry))

        presenter.onTextChanged("test")
        debounceScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)
        uiScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        val expectedDictionaryEntry = testEntry
        verify(translateUseCase, times(1)).execute(acualTranslatedWord)
        verify(translationViewState, times(1)).showLoading()
        verify(translationViewState, times(1)).showTranslation(expectedDictionaryEntry)
        verify(translationViewState, times(1)).hideLoading()
        verify(translationViewState, never()).showError(ArgumentMatchers.anyString())
    }

    @Test
    fun shouldShowErrorOnTranslationError() {
        val translatedWord = TranslatedWord(
            word = "test",
            fromLanguage = Language.EN,
            toLanguage = Language.RU
        )
        `when`(translateUseCase.execute(translatedWord)).thenReturn(Single.error(Throwable("test_error")))

        presenter.onTextChanged("test")
        debounceScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)
        uiScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        verify(translateUseCase, times(1)).execute(translatedWord)
        verify(translationViewState, times(1)).showLoading()
        verify(translationViewState, times(1)).showError("test_error")
        verify(translationViewState, times(1)).hideLoading()
        verify(translationViewState, never()).showTranslation(ArgumentMatchers.any(DictionaryEntry::class.java))
    }

    @Test
    fun shouldSetProperFromLanguage() {
        val actual = Language.DE
        presenter.setFromLanguage(actual)

        val expected = Language.DE
        verify(translationViewState, times(1)).setFromLanguage(expected)
    }

    @Test
    fun shouldSetProperToLanguage() {
        val actual = Language.ES
        presenter.setToLanguage(actual)

        val expected = Language.ES
        verify(translationViewState, times(1)).setToLanguage(expected)
    }

    @Test
    fun shouldSwitchLanguages() {
        presenter.switchLanguage()

        val expectedFromLanguage = Language.RU
        val expectedToLanguage = Language.EN
        verify(translationViewState, times(1)).setFromLanguage(expectedFromLanguage)
        verify(translationViewState, times(1)).setToLanguage(expectedToLanguage)
    }
}