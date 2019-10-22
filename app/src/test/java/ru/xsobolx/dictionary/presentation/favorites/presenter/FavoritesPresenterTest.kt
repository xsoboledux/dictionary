package ru.xsobolx.dictionary.presentation.favorites.presenter

import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.xsobolx.dictionary.RxRule
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.MakeTranslationFavoriteUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.testEntry
import ru.xsobolx.dictionary.presentation.favorites.view.FavoritesView
import ru.xsobolx.dictionary.presentation.favorites.view.`FavoritesView$$State`

class FavoritesPresenterTest {

    @Rule
    @JvmField
    val rule = RxRule()

    @Mock
    private lateinit var getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase
    @Mock
    private lateinit var makeTranslationFavoriteUseCase: MakeTranslationFavoriteUseCase
    @Mock
    private lateinit var favoritesView: FavoritesView
    @Mock
    private lateinit var favoritesViewState: `FavoritesView$$State`
    private lateinit var presenter: FavoritesPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = FavoritesPresenter(
            getAllSavedTranslationUseCase,
            makeTranslationFavoriteUseCase
        )
        presenter.setViewState(favoritesViewState)
    }

    @Test
    fun shouldShowFavoriteTranslationsOnAttach() {
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.just(getMixedEntries()))

        presenter.attachView(favoritesView)

        val expect = getFavoritesEntries()
        rule.scheduler.triggerActions()

        verify(favoritesView, times(1)).showLoading()
        verify(favoritesViewState, times(1)).hideLoading()
        verify(favoritesViewState, times(1)).showFavorites(expect)
        verify(favoritesViewState, never()).showError(anyString())
    }

    @Test
    fun shouldShowError() {
        val actualError = Throwable("error")
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.error(actualError))

        presenter.attachView(favoritesView)
        rule.scheduler.triggerActions()

        val expect = "error"

        verify(favoritesViewState, times(1)).showError(expect)
        verify(favoritesViewState, never()).showFavorites(anyList())
    }

    @Test
    fun shouldDeleteFromFavorites() {
        val entryToClick = testEntry2().copy(isFavorite = testEntry2().isFavorite)
        `when`(makeTranslationFavoriteUseCase.execute(entryToClick)).thenReturn(Single.just(Unit))
        `when`(getAllSavedTranslationUseCase.execute(Unit)).thenReturn(Single.just(getMixedEntries()))

        presenter.attachView(favoritesView)
        rule.scheduler.triggerActions()
        val favoritesOnAttach = getFavoritesEntries()
        verify(favoritesViewState, times(1)).showFavorites(favoritesOnAttach)

        presenter.onFavoriteClick(entryToClick)

        val expected = listOf(testEntry3())
        verify(favoritesViewState, times(1)).showFavorites(expected)
    }

    private fun getMixedEntries(): List<DictionaryEntry> {
        val entry1 = testEntry
        val entry2 = testEntry2()
        val entry3 = testEntry3()
        val entry4 = testEntry.copy(word = "test4")
        return listOf(entry1, entry2, entry3, entry4)
    }

    private fun getFavoritesEntries(): List<DictionaryEntry> {
        val entry2 = testEntry2()
        val entry3 = testEntry3()
        return listOf(entry2, entry3)
    }

    private fun testEntry3() = testEntry.copy(word = "test3", isFavorite = true)

    private fun testEntry2() = testEntry.copy(word = "test2", isFavorite = true)
}