package ru.xsobolx.dictionary.presentation.phrasebook.presenter

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import moxy.InjectViewState
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.MakeTranslationFavoriteUseCase
import ru.xsobolx.dictionary.domain.translation.SearchTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BasePresenter
import ru.xsobolx.dictionary.presentation.phrasebook.view.PhraseBookView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class PhraseBookPresenter
@Inject constructor(
    private val getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase,
    private val searchTranslationUseCase: SearchTranslationUseCase,
    private val makeTranslationFavoriteUseCase: MakeTranslationFavoriteUseCase
) : BasePresenter<PhraseBookView>() {
    private val textSubject = PublishSubject.create<String>()
    private val onFavoriteClickSubject = PublishSubject.create<DictionaryEntry>()

    override fun onAttach(view: PhraseBookView?) {
        val getAllSavedTranslationsObservable = Observable.defer {
            getAllSavedTranslationUseCase.execute(Unit)
                .map { it.distinct() }
                .toObservable()
        }.share()

        val searchTranslations = textSubject
            .startWith("")
            .switchMap { text ->
                if (text.isEmpty()) {
                    return@switchMap getAllSavedTranslationsObservable
                }
                searchTranslationUseCase.execute(text)
                    .toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach { view?.showLoading() }
            .subscribe(::handleSuccessGetAllTranslations, ::handleErrorGetAllTranslations)
        subscriptions.add(searchTranslations)

        val makeFavoriteSubscription = onFavoriteClickSubject
            .throttleLast(200, TimeUnit.MILLISECONDS)
            .switchMapSingle { makeTranslationFavoriteUseCase.execute(it) }
            .switchMap { getAllSavedTranslationsObservable }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccessGetAllTranslations, ::handleErrorGetAllTranslations)
        subscriptions.add(makeFavoriteSubscription)
    }

    private fun handleSuccessGetAllTranslations(translations: List<DictionaryEntry>) {
        Log.d("PHRASEBOOK", "translations: $translations".toUpperCase())
        viewState?.hideLoading()
        viewState?.showEntries(translations)
    }

    private fun handleErrorGetAllTranslations(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onTextChanged(text: String) {
        textSubject.onNext(text)
    }

    fun onFavoriteClick(dictionaryEntry: DictionaryEntry) {
        val newEntry = dictionaryEntry.copy(isFavorite = !dictionaryEntry.isFavorite)
        onFavoriteClickSubject.onNext(newEntry)
    }
}
