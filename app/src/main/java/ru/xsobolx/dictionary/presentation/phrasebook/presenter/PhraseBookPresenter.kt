package ru.xsobolx.dictionary.presentation.phrasebook.presenter

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import moxy.InjectViewState
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.SearchTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BasePresenter
import ru.xsobolx.dictionary.presentation.phrasebook.view.PhraseBookView
import javax.inject.Inject

@InjectViewState
class PhraseBookPresenter
@Inject constructor(
    private val getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase,
    private val searchTranslationUseCase: SearchTranslationUseCase
) : BasePresenter<PhraseBookView>() {
    private val textSubject = PublishSubject.create<String>()

    override fun onAttach(view: PhraseBookView?) {
        val getAllSavedTranslationsObservable = Observable.defer {
            getAllSavedTranslationUseCase.execute(null)
                .toObservable()
        }
        val searchTranslations = textSubject
            .startWith("")
            .switchMap { text ->
                if (text.isEmpty()) {
                    return@switchMap getAllSavedTranslationsObservable
                }
                searchTranslationUseCase.execute(text)
                    .toObservable()
            }
            .doOnEach { view?.showLoading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccessGetAllTranslations, ::handleErrorGetAllTranslations)

        subscriptions.add(searchTranslations)
    }

    private fun handleSuccessGetAllTranslations(translations: List<DictionaryEntry>) {
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
}
