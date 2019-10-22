package ru.xsobolx.dictionary.presentation.phrasebook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.app.DictionaryApp
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.RxTextWather
import ru.xsobolx.dictionary.presentation.base.adapter.PhrasebookAdapter
import ru.xsobolx.dictionary.presentation.phrasebook.presenter.PhraseBookPresenter
import ru.xsobolx.dictionary.presentation.phrasebook.view.PhraseBookView
import javax.inject.Inject

class PhrasebookFragment : MvpAppCompatFragment(), PhraseBookView {

    @InjectPresenter
    lateinit var phraseBookPresenter: PhraseBookPresenter

    @Inject
    lateinit var daggerPresenter: Lazy<PhraseBookPresenter>

    @ProvidePresenter
    fun providePresenter(): PhraseBookPresenter = daggerPresenter.get()

    private lateinit var searchEditText: EditText
    private lateinit var phrasebookRecycler: RecyclerView
    private lateinit var phrasebookAdapter: PhrasebookAdapter
    private lateinit var progress: ProgressBar
    private lateinit var textWatcher: RxTextWather
    private var textWatchDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phrasebook, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as DictionaryApp).appComponent?.translationComponent()
            ?.phrasebookScreenComponent()
            ?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress = view.findViewById(R.id.phrasebook_progress_bar)
        textWatcher = RxTextWather()
        searchEditText = view.findViewById<EditText>(R.id.search_edit_text).apply {
            addTextChangedListener(textWatcher)
        }
        textWatchDisposable = textWatcher.observeTextChanges()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({phraseBookPresenter.onTextChanged(it)}, {Log.e("PrasebookFragment", it.localizedMessage)})

        phrasebookAdapter = PhrasebookAdapter(
            onFavoriteButtonClick =  {
                phraseBookPresenter.onFavoriteClick(it) }
        )
        phrasebookRecycler = view.findViewById<RecyclerView>(R.id.phrasebook_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = phrasebookAdapter
        }
    }

    override fun showEntries(entries: List<DictionaryEntry>) {
        phrasebookAdapter.updateItems(entries.toMutableList())
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
}