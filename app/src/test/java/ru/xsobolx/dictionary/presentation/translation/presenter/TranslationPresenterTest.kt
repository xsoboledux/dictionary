package ru.xsobolx.dictionary.presentation.translation.presenter

import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.RxRule
import ru.xsobolx.dictionary.domain.translation.*
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslationDirection
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import ru.xsobolx.dictionary.presentation.translation.view.`TranslationView$$State`
import java.util.concurrent.TimeUnit

class TranslationPresenterTest {

    @Rule
    @JvmField
    val rule = RxRule()

    @Mock
    private lateinit var translationView: TranslationView
    @Mock
    private lateinit var translationViewState: `TranslationView$$State`
    @Mock
    private lateinit var translateUseCase: TranslateUseCase
    @Mock
    private lateinit var getLaguageUseCase: GetLanguageUseCase
    @Mock
    private lateinit var setLanguagesUseCase: SetLanguageUseCase
    @Mock
    private lateinit var getAllLanguagesUseCase: GetAllLanguagesUseCase
    private lateinit var presenter: TranslationPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter =
            TranslationPresenter(
                translateUseCase,
                getLaguageUseCase,
                getAllLanguagesUseCase,
                setLanguagesUseCase
            )
    }

    @Test
    fun shouldShowLanguagesOnFirstAttach() {
        setLanguages()

        presenter.setViewState(translationViewState)
        presenter.attachView(translationView)
        rule.scheduler.triggerActions()

        val expectedViewModel = fromENToRULanguageModel
        verify(getAllLanguagesUseCase, times(1)).execute(Unit)
        verify(getLaguageUseCase, times(1)).execute(TranslationDirection.FROM)
        verify(getLaguageUseCase, times(1)).execute(TranslationDirection.TO)
        verify(translationViewState, times(1)).showLanguages(expectedViewModel)
    }

    @Test
    fun shouldShowTranslationOnTextChanged() {
        setLanguages()
        val actualTranslatedWord = testTranslatedWord
        `when`(translateUseCase.execute(actualTranslatedWord)).thenReturn(Single.just(testEntry))

        presenter.setViewState(translationViewState)
        presenter.attachView(translationView)
        presenter.onTextChanged(translatedWord = actualTranslatedWord)
        rule.scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        val expectedDictionaryEntry = testEntry
        verify(translateUseCase, times(1)).execute(actualTranslatedWord)
        verify(translationViewState, times(1)).showTranslation(expectedDictionaryEntry)
        verify(translationViewState, never()).showError(ArgumentMatchers.anyString())
    }

    @Test
    fun shouldShowErrorOnTranslationError() {
        setLanguages()
        val translatedWord = testTranslatedWord
        `when`(translateUseCase.execute(translatedWord)).thenReturn(Single.error(Throwable("test_error")))

        presenter.setViewState(translationViewState)
        presenter.attachView(translationView)
        presenter.onTextChanged(translatedWord)
        rule.scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        verify(translateUseCase, times(1)).execute(translatedWord)
        verify(translationViewState, times(1)).showLoading()
        verify(translationViewState, times(1)).showError("test_error")
        verify(translationViewState, never()).showTranslation(ArgumentMatchers.any(DictionaryEntry::class.java))
    }

    @Test
    fun shouldSwitchLanguages() {
        setLanguages()
        val actualLanguagesViewModel = fromENToRULanguageModel
        `when`(setLanguagesUseCase.execute(fromRuLanguageEntity)).thenReturn(
            Single.just(
                fromRuLanguageEntity
            )
        )
        `when`(setLanguagesUseCase.execute(toEnLanguageEntity)).thenReturn(
            Single.just(
                toEnLanguageEntity
            )
        )

        presenter.setViewState(translationViewState)
        presenter.attachView(translationView)
        presenter.onSwitchLanguages(actualLanguagesViewModel)
        rule.scheduler.triggerActions()

        val expectedFromLanguage = Language.RU
        verify(translationViewState, times(1)).setFromLanguage(expectedFromLanguage)
        val expectedToLanguage = Language.EN
        verify(translationViewState, times(1)).setToLanguage(expectedToLanguage)
    }

    @Test
    fun shouldUnsubscribeOnDetachView() {
        presenter.detachView(translationView)
        assert(presenter.isSubscriptionsEmpty())
    }

    private fun setLanguages() {
        val actualLanguages = testLanguagesSet
        val actualFromLanguage = fromEnLanguageEntity
        val actualToLanguage = toRuLanguageEntity
        `when`(getAllLanguagesUseCase.execute(Unit)).thenReturn(Single.just(actualLanguages))
        `when`(getLaguageUseCase.execute(TranslationDirection.FROM)).thenReturn(
            Single.just(
                actualFromLanguage
            )
        )
        `when`(getLaguageUseCase.execute(TranslationDirection.TO)).thenReturn(
            Single.just(
                actualToLanguage
            )
        )
    }
}