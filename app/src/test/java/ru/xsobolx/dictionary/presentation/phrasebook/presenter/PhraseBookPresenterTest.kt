package ru.xsobolx.dictionary.presentation.phrasebook.presenter

import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ru.xsobolx.dictionary.RxRule
import ru.xsobolx.dictionary.domain.translation.*
import ru.xsobolx.dictionary.presentation.phrasebook.view.PhraseBookView
import ru.xsobolx.dictionary.presentation.phrasebook.view.`PhraseBookView$$State`

@RunWith(MockitoJUnitRunner::class)
class PhraseBookPresenterTest {

    @Rule
    @JvmField
    val rule = RxRule()

    @Mock
    private lateinit var getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase
    @Mock
    private lateinit var searchTranslationUseCase: SearchTranslationUseCase
    @Mock
    private lateinit var phraseBookView: PhraseBookView
    @Mock
    private lateinit var phraseBookViewState: `PhraseBookView$$State`
    @Mock
    private lateinit var makeTranslationFavoriteUseCase: MakeTranslationFavoriteUseCase
    private lateinit var presenter: PhraseBookPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PhraseBookPresenter(
            getAllSavedTranslationUseCase,
            searchTranslationUseCase,
            makeTranslationFavoriteUseCase
        )
        presenter.setViewState(phraseBookViewState)
    }

    @Test
    fun shouldShowAllSavedTranslationsOnFirstAttach() {
        val actual = listOf(testEntry)
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.just(actual))

        presenter.attachView(phraseBookView)
        rule.scheduler.triggerActions()

        val expect = listOf(testEntry)
        verifyZeroInteractions(searchTranslationUseCase)
        verify(phraseBookView, times(1)).showLoading()
        verify(phraseBookViewState, times(1)).showEntries(expect)
        verify(phraseBookViewState, times(1)).hideLoading()
        verify(phraseBookViewState, never()).showError(ArgumentMatchers.anyString())
    }

    @Test
    fun shouldSearchTranslationsOnSearchByWord() {
        val allEntries = listOf(testEntry, testEntry2, testEntry3)
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.just(allEntries))

        presenter.attachView(phraseBookView)
        presenter.onTextChanged("test")
        rule.scheduler.triggerActions()

        val expectSearched = listOf(testEntry)
        verify(phraseBookViewState).showEntries(expectSearched)
        verify(phraseBookViewState, never()).showError(ArgumentMatchers.anyString())
    }

    @Test
    fun shouldSearchTranslationsOnSearchByTranslation() {
        val allEntries = listOf(testEntry, testEntry2, testEntry3)
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.just(allEntries))

        presenter.attachView(phraseBookView)
        presenter.onTextChanged("найти")
        rule.scheduler.triggerActions()

        val expected = listOf(testEntry2)
        verify(phraseBookViewState).showEntries(expected)
        verify(phraseBookViewState, never()).showError(ArgumentMatchers.anyString())
    }

    @Test
    fun shouldShowError() {
        val actualError = Throwable(message = "test_error")
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.error(actualError))

        presenter.attachView(phraseBookView)
        rule.scheduler.triggerActions()

        verify(phraseBookViewState, never()).showEntries(ArgumentMatchers.anyList())
        verify(phraseBookViewState, times(1)).showError("test_error")
    }
}