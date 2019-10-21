package ru.xsobolx.dictionary.presentation.translation.presenter

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.subjects.BehaviorSubject
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
    private val fromLanguageSubject = BehaviorSubject.create<Language>()
    private val toLanguageSubject = BehaviorSubject.create<Language>()
    private var switchtLanguageSubscription: Disposable? = null

    override fun onAttach(view: TranslationView?) {
        val allLanguages = getAllLanguagesUseCase.execute(Unit)
        val fromLanguage = getLanguageUseCase.execute(TranslationDirection.FROM)
        val toLanguage = getLanguageUseCase.execute(TranslationDirection.TO)

        val viewStateSubscription = Single.zip(
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
        subscriptions.add(viewStateSubscription)

        val translateSubscription = Observable.zip(
            fromLanguageSubject,
            toLanguageSubject,
            BiFunction<Language, Language, Pair<Language, Language>> { from, to ->
                print("from subj: $from")
                print("to subj: $to")
                from to to })
            .withLatestFrom(
                textSubject,
                BiFunction<Pair<Language, Language>, String, TranslatedWord> { langPair, text ->
                    val (from, to) = langPair
                    TranslatedWord(text, from, to)
                })
            .debounce(TEXT_INPUT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
            .switchMapSingle { translationUseCase.execute(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccessTranslation, ::handleError)
        subscriptions.add(translateSubscription)

        val setFromLanguageSubscription = fromLanguageSubject.switchMapSingle { lang ->
            val languageEntity = LanguageEntity(lang, TranslationDirection.FROM)
            setLanguageUseCase.execute(languageEntity)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.setFromLanguage(it.language) }, ::handleError)
        subscriptions.add(setFromLanguageSubscription)

        val setToLanguageSubscription = toLanguageSubject.switchMapSingle { lang ->
            val languageEntity = LanguageEntity(lang, TranslationDirection.TO)
            setLanguageUseCase.execute(languageEntity)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.setToLanguage(it.language) }, ::handleError)
        subscriptions.add(setToLanguageSubscription)
    }

    private fun showLanguages(viewModel: TranslateScreenLanguagedViewModel) {
        fromLanguageSubject.onNext(viewModel.fromLanguage)
        toLanguageSubject.onNext(viewModel.toLanguage)
        viewState?.hideLoading()
        viewState?.render(viewModel)
    }

    private fun handleSuccessTranslation(dictionaryEntry: DictionaryEntry) {
        viewState?.hideLoading()
        viewState?.showTranslation(dictionaryEntry)
    }

    private fun handleError(error: Throwable) {
        print("error: $error" )
        viewState?.hideLoading()
        viewState?.showError(error.message)
    }

    fun onFromLanguageChanged(language: Language) {
        fromLanguageSubject.onNext(language)
    }

    fun onToLanguageChanged(language: Language) {
        toLanguageSubject.onNext(language)
    }

    fun onTextChanged(text: String) {
        textSubject.onNext(text)
    }

    fun switchLanguage() {
        switchtLanguageSubscription = Observable.zip(
            fromLanguageSubject,
            toLanguageSubject,
            BiFunction<Language, Language, Pair<Language, Language>> { from, to -> from to to }
        )
            .subscribe { langPair ->
                val (from, to) = langPair
                onFromLanguageChanged(to)
                onToLanguageChanged(from)
            }
        subscriptions.add(switchtLanguageSubscription!!)
    }
}

data class TranslateScreenLanguagedViewModel(
    val allLanguages: Set<Language>,
    val fromLanguage: Language,
    val toLanguage: Language
)