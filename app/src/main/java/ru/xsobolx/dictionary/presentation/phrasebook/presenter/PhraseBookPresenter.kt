package ru.xsobolx.dictionary.presentation.phrasebook.presenter

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Scheduler
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
    private val searchTranslationUseCase: SearchTranslationUseCase,
    private val uiScheduler: Scheduler
    ) : BasePresenter<PhraseBookView>() {

    override fun onAttach(view: PhraseBookView?) {
        val getAllSavedTranslationsSubscription = getAllSavedTranslationUseCase.execute(null)
            .observeOn(uiScheduler)
            .subscribe(::handleSuccessGetAllTranslations, ::handleErrorGetAllTranslations)
        subscriptions.add(getAllSavedTranslationsSubscription)
    }

    private fun handleSuccessGetAllTranslations(translations: List<DictionaryEntry>) {
        viewState.hideLoading()
        viewState?.showEntries(translations)
    }

    private fun handleErrorGetAllTranslations(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }
}
