package ru.xsobolx.dictionary.presentation.translation.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import moxy.InjectViewState
import ru.xsobolx.dictionary.domain.translation.TranslateUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import ru.xsobolx.dictionary.presentation.base.BasePresenter
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TEXT_INPUT_DELAY_IN_MILLISECONDS = 500L


@InjectViewState
class TranslationPresenter
@Inject constructor(
    private val translationUseCase: TranslateUseCase
) : BasePresenter<TranslationView>() {
    private val textSubject = PublishSubject.create<String>()
    private lateinit var fromLanguage: Language
    private lateinit var toLanguage: Language

    override fun onAttach(view: TranslationView?) {
        if (!this::fromLanguage.isInitialized) {
            throw AssertionError("fromLanguage must be initialized")
        }
        if (!this::toLanguage.isInitialized) {
            throw AssertionError("toLanguage must be initialized")
        }
        val translateSubscription =
            textSubject.debounce(TEXT_INPUT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
                .switchMap { text ->
                    val translatedWord = TranslatedWord(text, fromLanguage, toLanguage)
                    translationUseCase.execute(translatedWord)
                        .toObservable()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach { viewState?.showLoading() }
                .subscribe(::handleSuccessTranslation, ::handleError)
        subscriptions.add(translateSubscription)
    }

    private fun handleSuccessTranslation(dictionaryEntry: DictionaryEntry) {
        viewState?.hideLoading()
        viewState?.showTranslation(dictionaryEntry)
    }

    private fun handleError(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onFromLanguageChanged(language: Language) {
        fromLanguage = language
    }

    fun onToLanguageChanged(language: Language) {
        toLanguage = language
    }

    fun onTextChanged(text: String) {
        textSubject.onNext(text)
    }

    fun switchLanguage() {
        val temp = fromLanguage
        fromLanguage = toLanguage
        toLanguage = temp
        viewState?.setFromLanguage(fromLanguage)
        viewState?.setToLanguage(toLanguage)
    }
}