package ru.xsobolx.dictionary.presentation.translation.presenter

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.subjects.PublishSubject
import moxy.InjectViewState
import ru.xsobolx.dictionary.domain.translation.GetAllLanguagesUseCase
import ru.xsobolx.dictionary.domain.translation.GetLanguageUseCase
import ru.xsobolx.dictionary.domain.translation.SetLanguageUseCase
import ru.xsobolx.dictionary.domain.translation.TranslateUseCase
import ru.xsobolx.dictionary.domain.translation.model.*
import ru.xsobolx.dictionary.presentation.base.BasePresenter
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TIME_OUT_IN_MILLISECONDS = 500L

@InjectViewState
class TranslationPresenter
@Inject constructor(
    private val translationUseCase: TranslateUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getAllLanguagesUseCase: GetAllLanguagesUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : BasePresenter<TranslationView>() {
    private val translateSubject = PublishSubject.create<TranslatedWord>()

    override fun onAttach(view: TranslationView?) {
        loadLanguages()

        val translateSubscription = translateSubject.filter { it.word.isNotEmpty() }
            .debounce(TIME_OUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
            .switchMapSingle { translationUseCase.execute(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccessTranslation, ::handleError)
        subscriptions.add(translateSubscription)
    }

    private fun loadLanguages() {
        val allLanguages = getAllLanguagesUseCase.execute(Unit)
        val fromLanguage = getLanguageUseCase.execute(TranslationDirection.FROM)
        val toLanguage = getLanguageUseCase.execute(TranslationDirection.TO)

        val showLanguagesSubscription = Single.zip(
            allLanguages,
            fromLanguage,
            toLanguage,
            Function3<Set<Language>, LanguageEntity, LanguageEntity, LanguagesViewModel> { all, from, to ->
                LanguagesViewModel(
                    allLanguages = all,
                    fromLanguage = from.language,
                    toLanguage = to.language
                )
            }
        )
            .doOnSubscribe { viewState?.showLoading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::showLanguages, ::handleError)
        subscriptions.add(showLanguagesSubscription)
    }

    private fun showLanguages(viewModel: LanguagesViewModel) {
        viewState?.hideLoading()
        viewState?.showLanguages(viewModel)
    }

    private fun handleError(error: Throwable) {
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onFromLanguageChanged(language: Language) {
        val setFromLanguageSubscription =
            setLanguageUseCase.execute(LanguageEntity(language, TranslationDirection.FROM))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.setFromLanguage(it.language) }, ::handleError)
        subscriptions.add(setFromLanguageSubscription)
    }

    fun onToLanguageChanged(language: Language) {
        val setToLanguageSubscription =
            setLanguageUseCase.execute(LanguageEntity(language, TranslationDirection.TO))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.setToLanguage(it.language) }, ::handleError)
        subscriptions.add(setToLanguageSubscription)
    }

    fun onTextChanged(translatedWord: TranslatedWord) {
        translateSubject.onNext(translatedWord)
    }

    private fun handleSuccessTranslation(dictionaryEntry: DictionaryEntry) {
        viewState?.hideLoading()
        viewState?.showTranslation(dictionaryEntry)
    }

    fun onSwitchLanguages(languages: LanguagesViewModel) {
        onToLanguageChanged(languages.fromLanguage)
        onFromLanguageChanged(languages.toLanguage)
    }
}

data class LanguagesViewModel(
    val allLanguages: Set<Language>,
    val fromLanguage: Language,
    val toLanguage: Language
)
