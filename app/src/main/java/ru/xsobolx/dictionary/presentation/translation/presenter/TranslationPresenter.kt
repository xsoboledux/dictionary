package ru.xsobolx.dictionary.presentation.translation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import ru.xsobolx.dictionary.domain.translation.TranslateUseCase
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TEXT_INPUT_DELAY_IN_MILLISECONDS = 500L

@InjectViewState
class TranslationPresenter
@Inject constructor(
    private val translationUseCase: TranslateUseCase,
    private val uiScheduler: Scheduler,
    private val debounceScheduler: Scheduler
) : MvpPresenter<TranslationView>() {
    private val textSubject = PublishSubject.create<String>()
    private lateinit var fromLanguage: Language
    private lateinit var toLanguage: Language
    private val subscriptions = CompositeDisposable()

    override fun attachView(view: TranslationView?) {
        super.attachView(view)
        if (!this::fromLanguage.isInitialized) {
            throw AssertionError("fromLanguage must be initialized")
        }
        if (!this::toLanguage.isInitialized) {
            throw AssertionError("toLanguage must be initialized")
        }
        val translateSubscription =
            textSubject.debounce(TEXT_INPUT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS, debounceScheduler)
                .switchMap { text ->
                    val translatedWord = TranslatedWord(text, fromLanguage, toLanguage)
                    translationUseCase.execute(translatedWord)
                        .doOnSubscribe { viewState?.showLoading() }
                        .toObservable()
                        .observeOn(uiScheduler)
                }
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

    override fun detachView(view: TranslationView?) {
        super.detachView(view)
        subscriptions.dispose()
    }

    fun setFromLanguage(language: Language) {
        fromLanguage = language
        viewState?.setFromLanguage(fromLanguage)
    }

    fun setToLanguage(language: Language) {
        toLanguage = language
        viewState?.setToLanguage(toLanguage)
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

    fun isSubscriptionsEmpty() = subscriptions.size() == 0
}