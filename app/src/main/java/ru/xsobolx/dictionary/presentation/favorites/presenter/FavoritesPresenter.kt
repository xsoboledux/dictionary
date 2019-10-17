package ru.xsobolx.dictionary.presentation.favorites.presenter

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
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
    private val onFavoriteClickSubject = PublishSubject.create<DictionaryEntry>()

    override fun onAttach(view: FavoritesView?) {
        val favoritesObservable = Observable.defer {
            getAllSavedTranslationUseCase.execute(Unit)
                .toObservable()
        }
            .doOnNext { view?.showLoading() }
            .map { it.filter { entry -> entry.isFavorite } }
            .share()

        val favoritesSubscription = favoritesObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccessGetFavoriteTranslations, ::handleError)
        subscriptions.add(favoritesSubscription)

        val onFavoriteClickSubscription = onFavoriteClickSubject
            .map { entry ->
                val isFavorite = !entry.isFavorite
                entry.copy(isFavorite = isFavorite)
            }
            .switchMapSingle(makeTranslationFavoriteUseCase::execute)
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap { favoritesObservable }
            .subscribe(::handleSuccessGetFavoriteTranslations, ::handleError)
        subscriptions.add(onFavoriteClickSubscription)
    }

    private fun handleSuccessGetFavoriteTranslations(entries: List<DictionaryEntry>) {
        viewState?.hideLoading()
        viewState?.showFavorites(entries)
    }

    private fun handleError(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onFavoriteClick(entry: DictionaryEntry) {
        onFavoriteClickSubject.onNext(entry)
    }
}