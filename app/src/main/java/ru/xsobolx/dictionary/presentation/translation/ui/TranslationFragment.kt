package ru.xsobolx.dictionary.presentation.translation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dagger.Lazy
import io.reactivex.disposables.Disposable
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.app.DictionaryApp
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.TranslatedWord
import ru.xsobolx.dictionary.presentation.base.RxTextWather
import ru.xsobolx.dictionary.presentation.translation.presenter.LanguagesViewModel
import ru.xsobolx.dictionary.presentation.translation.presenter.TranslationPresenter
import ru.xsobolx.dictionary.presentation.translation.view.TranslationView
import javax.inject.Inject

class TranslationFragment : MvpAppCompatFragment(), TranslationView {

    @InjectPresenter
    lateinit var translationPresenter: TranslationPresenter

    @Inject
    lateinit var daggerPresenter: Lazy<TranslationPresenter>

    private lateinit var fromLanguageSpinner: Spinner
    private lateinit var toLanguageSpinner: Spinner
    private lateinit var switchLanguagesButton: ImageButton
    private lateinit var translateEditText: EditText
    private lateinit var translationTextView: TextView
    private lateinit var progress: ProgressBar

    private lateinit var fromLanguageAdapter: ArrayAdapter<Language>
    private lateinit var toLanguageAdapter: ArrayAdapter<Language>
    private lateinit var textWatcher: RxTextWather

    private var textListenSubscription: Disposable? = null
    private var languagesViewModel: LanguagesViewModel? = null

    @ProvidePresenter
    fun providePresenter(): TranslationPresenter = daggerPresenter.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as DictionaryApp).appComponent?.translationComponentBuilder()
            ?.build()
            ?.translationScreenComponentBuilder()
            ?.build()
            ?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews(view)
        setLanguagesSpinners()

        switchLanguagesButton.setOnClickListener {
            translationPresenter.onSwitchLanguages(languagesViewModel!!)
        }
        setUpEditText()
    }

    private fun setUpEditText() {
        textWatcher = RxTextWather()
        translateEditText.addTextChangedListener(textWatcher)
        if (textListenSubscription != null) {
            textListenSubscription?.dispose()
        }
        textListenSubscription = textWatcher.observeTextChanges()
            .filter { it.isNotEmpty() }
            .subscribe { text ->
                val translatedWord = TranslatedWord(
                    word = text,
                    fromLanguage = languagesViewModel?.fromLanguage!!,
                    toLanguage = languagesViewModel?.toLanguage!!
                )
                translationPresenter.onTextChanged(translatedWord)
            }
    }

    private fun setLanguagesSpinners() {
        fromLanguageAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item)
        fromLanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        toLanguageAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item)
        toLanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromLanguageSpinner.adapter = fromLanguageAdapter
        fromLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                translationPresenter.onFromLanguageChanged(p0?.selectedItem as Language)
            }
        }
        toLanguageSpinner.adapter = toLanguageAdapter
        toLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                translationPresenter.onToLanguageChanged(p0?.selectedItem as Language)
            }

        }
    }

    private fun setViews(view: View) {
        fromLanguageSpinner = view.findViewById(R.id.from_language_spinner)
        toLanguageSpinner = view.findViewById(R.id.to_language_spinner)
        switchLanguagesButton = view.findViewById(R.id.switch_language_button)
        translateEditText = view.findViewById(R.id.translate_edit_text)
        translationTextView = view.findViewById(R.id.translation_text_view)
        progress = view.findViewById(R.id.translate_progress)
    }

    override fun showTranslation(dictionaryEntry: DictionaryEntry) {
        translationTextView.text = dictionaryEntry.translation
    }

    override fun setFromLanguage(language: Language) {
        languagesViewModel = languagesViewModel?.copy(fromLanguage = language)
        fromLanguageSpinner.setSelection(language.ordinal)
    }

    override fun setToLanguage(language: Language) {
        languagesViewModel = languagesViewModel?.copy(toLanguage = language)
        toLanguageSpinner.setSelection(language.ordinal)
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showLanguages(viewModel: LanguagesViewModel) {
        languagesViewModel = viewModel
        fromLanguageAdapter.addAll(viewModel.allLanguages)
        fromLanguageAdapter.notifyDataSetChanged()
        toLanguageAdapter.addAll(viewModel.allLanguages)
        toLanguageAdapter.notifyDataSetChanged()
        fromLanguageSpinner.setSelection(viewModel.fromLanguage.ordinal)
        toLanguageSpinner.setSelection(viewModel.toLanguage.ordinal)
    }
}