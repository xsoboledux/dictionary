package ru.xsobolx.dictionary.presentation.translation.presenter

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
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

private const val TEXT_INPUT_DELAY_IN_MILLISECONDS = 500L

@InjectViewState
class TranslationPresenter
@Inject constructor(
    private val translationUseCase: TranslateUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getAllLanguagesUseCase: GetAllLanguagesUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : BasePresenter<TranslationView>() {
    private val textSubject = PublishSubject.create<String>()
    private val fromLanguageSubject = PublishSubject.create<Language>()
    private val toLanguageSubject = PublishSubject.create<Language>()

    override fun onAttach(view: TranslationView?) {
        showLanguages()
        subscribeTranslation()
    }

    private fun subscribeTranslation() {
        val translateSubscription = Observable.zip(
            fromLanguageSubject,
            toLanguageSubject,
            BiFunction<Language, Language, Pair<Language, Language>> { from, to -> from to to }
        )
            .withLatestFrom(
                textSubject.startWith("").filter { it.isNotEmpty() },
                BiFunction<Pair<Language, Language>, String, TranslatedWord> { langs, text ->
                    TranslatedWord(
                        word = text,
                        fromLanguage = langs.first,
                        toLanguage = langs.second
                    )
                }
            )
            .debounce(TEXT_INPUT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
            .switchMapSingle { translationUseCase.execute(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccessTranslation, ::handleError)
        subscriptions.add(translateSubscription)
    }

    private fun showLanguages() {
        val allLanguages = getAllLanguagesUseCase.execute(Unit)
        val fromLanguage = getLanguageUseCase.execute(TranslationDirection.FROM)
        val toLanguage = getLanguageUseCase.execute(TranslationDirection.TO)

        val showLanguagesSubscription = Single.zip(
            allLanguages,
            fromLanguage,
            toLanguage,
            Function3<Set<Language>, LanguageEntity, LanguageEntity, TranslateScreenLanguagedViewModel> { all, from, to ->
                TranslateScreenLanguagedViewModel(
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

    private fun showLanguages(viewModel: TranslateScreenLanguagedViewModel) {
        fromLanguageSubject.onNext(viewModel.fromLanguage)
        toLanguageSubject.onNext(viewModel.toLanguage)
        viewState?.hideLoading()
        viewState?.showLanguages(viewModel)
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
        val setFromLanguageSubscription =
            setLanguageUseCase.execute(LanguageEntity(language, TranslationDirection.FROM))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState?.setFromLanguage(it.language) }, ::handleError)
        subscriptions.add(setFromLanguageSubscription)
    }

    fun onToLanguageChanged(language: Language) {
        val setToLanguageSubscription =
            setLanguageUseCase.execute(LanguageEntity(language, TranslationDirection.TO))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState?.setToLanguage(it.language) }, ::handleError)
        subscriptions.add(setToLanguageSubscription)
    }

    fun onTextChanged(text: String) {
        textSubject.onNext(text)
    }

    fun onSwitchLanguages() {
        val switchLanguagesSubscription = Observable.zip(
            fromLanguageSubject,
            toLanguageSubject,
            BiFunction<Language, Language, Pair<Language, Language>> { from, to -> from to to }
        )
            .switchMapSingle { langs ->
                val (from, to) = langs
                Single.zip(
                    setLanguageUseCase.execute(LanguageEntity(from, TranslationDirection.TO)),
                    setLanguageUseCase.execute(LanguageEntity(to, TranslationDirection.FROM)),
                    BiFunction<LanguageEntity, LanguageEntity, Pair<Language, Language>> { toLanguageEntity, fromLanguageEntity ->
                        toLanguageEntity.language to fromLanguageEntity.language
                    }
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSwitchLanguagesSuccess, ::handleError)
        subscriptions.add(switchLanguagesSubscription)
    }

    private fun handleSwitchLanguagesSuccess(toFromLanguagesPair: Pair<Language, Language>) {
        val (to, from) = toFromLanguagesPair
        viewState?.hideLoading()
        viewState?.setFromLanguage(from)
        viewState?.setToLanguage(to)
    }
}

data class TranslateScreenLanguagedViewModel(
    val allLanguages: Set<Language>,
    val fromLanguage: Language,
    val toLanguage: Language
)