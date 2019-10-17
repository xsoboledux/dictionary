package ru.xsobolx.dictionary.presentation.translation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslationPresenter
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import javax.inject.Inject
import javax.inject.Provider

class TranslationFragment : MvpAppCompatFragment(), TranslationView {

    @InjectPresenter
    lateinit var translationPresenter: TranslationPresenter

    @Inject
    lateinit var presenterProvider: Provider<TranslationPresenter>

    @ProvidePresenter
    fun providePresenter() = presenterProvider.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showTranslation(dictionaryEntry: DictionaryEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFromLanguage(language: Language) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setToLanguage(language: Language) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}