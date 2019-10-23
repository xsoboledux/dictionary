package ru.xsobolx.dictionary.presentation.favorites.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.MakeTranslationFavoriteUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BasePresenter
import ru.xsobolx.dictionary.presentation.favorites.view.FavoritesView
import javax.inject.Inject

@InjectViewState
class FavoritesPresenter
@Inject constructor(
    private val getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase,
    private val makeTranslationFavoriteUseCase: MakeTranslationFavoriteUseCase
) : BasePresenter<FavoritesView>() {

    override fun onAttach(view: FavoritesView?) {
        val allSubscription = getAllSavedTranslationUseCase.execute(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEvent { _, _ ->  view?.showLoading() }
            .subscribe(::showEntries, ::handleError)
        subscriptions.add(allSubscription)
    }

    private fun showEntries(entries: List<DictionaryEntry>) {
        viewState?.hideLoading()
        viewState?.showFavorites(entries.filter { it.isFavorite })
    }

    private fun handleError(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onFavoriteClick(entry: DictionaryEntry) {
        val newEntry = entry.copy(isFavorite = !entry.isFavorite)
        val makeFavoriteSubscription = makeTranslationFavoriteUseCase.execute(newEntry)
            .flatMap { getAllSavedTranslationUseCase.execute(Unit) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState?.showLoading() }
            .subscribe(::showEntries, ::handleError)
        subscriptions.add(makeFavoriteSubscription)
    }
}