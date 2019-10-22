package ru.xsobolx.dictionary.presentation.phrasebook.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import ru.xsobolx.dictionary.domain.translation.GetAllSavedTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.MakeTranslationFavoriteUseCase
import ru.xsobolx.dictionary.domain.translation.SearchTranslationUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BasePresenter
import ru.xsobolx.dictionary.presentation.phrasebook.view.PhraseBookView
import javax.inject.Inject

@InjectViewState
class PhraseBookPresenter
@Inject constructor(
    private val getAllSavedTranslationUseCase: GetAllSavedTranslationUseCase,
    private val searchTranslationUseCase: SearchTranslationUseCase,
    private val makeTranslationFavoriteUseCase: MakeTranslationFavoriteUseCase
) : BasePresenter<PhraseBookView>() {
    private var searchString: String = ""

    override fun onAttach(view: PhraseBookView?) {
        val allSubscription = getAllSavedTranslationUseCase.execute(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEvent { _, _ -> view?.showLoading() }
            .subscribe(::showEntries, ::handleErrorGetAllTranslations)
        subscriptions.add(allSubscription)
    }

    private fun searchPredicate(
        dictionaryEntry: DictionaryEntry,
        text: String
    ) = dictionaryEntry.word.contains(text) || dictionaryEntry.translation.contains(text)

    private fun showEntries(translations: List<DictionaryEntry>) {
        viewState?.hideLoading()
        viewState?.showEntries(translations.filter { dictionaryEntry ->
            searchPredicate(
                dictionaryEntry,
                searchString
            )
        })
    }

    private fun handleErrorGetAllTranslations(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onTextChanged(text: String) {
        val searchSubscription = getAllSavedTranslationUseCase.execute(Unit)
            .map { translations ->
                searchString = text
                translations
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState?.showLoading() }
            .subscribe(::showEntries, ::handleErrorGetAllTranslations)
        subscriptions.add(searchSubscription)
    }

    fun onFavoriteClick(dictionaryEntry: DictionaryEntry) {
        val newEntry = dictionaryEntry.copy(isFavorite = !dictionaryEntry.isFavorite)
        val makeFavoriteSubscription = makeTranslationFavoriteUseCase.execute(newEntry)
            .flatMap { getAllSavedTranslationUseCase.execute(Unit) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState?.showLoading() }
            .subscribe(::showEntries, ::handleErrorGetAllTranslations)
        subscriptions.add(makeFavoriteSubscription)
    }
}
