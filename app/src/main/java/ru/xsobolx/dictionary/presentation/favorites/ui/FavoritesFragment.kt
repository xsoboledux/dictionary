package ru.xsobolx.dictionary.presentation.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Lazy
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.app.DictionaryApp
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.adapter.PhrasebookAdapter
import ru.xsobolx.dictionary.presentation.favorites.presenter.FavoritesPresenter
import ru.xsobolx.dictionary.presentation.favorites.view.FavoritesView
import javax.inject.Inject

class FavoritesFragment : MvpAppCompatFragment(), FavoritesView {

    @InjectPresenter
    lateinit var favoritesPresenter: FavoritesPresenter

    @Inject
    lateinit var daggerPresenter: Lazy<FavoritesPresenter>

    @ProvidePresenter
    fun providePresenter(): FavoritesPresenter = daggerPresenter.get()

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: PhrasebookAdapter
    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as DictionaryApp).appComponent?.translationComponent()
            ?.favoritesScreenComponent()
            ?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress = view.findViewById(R.id.favorites_progress_bar)
        favoritesAdapter = PhrasebookAdapter(
            onFavoriteButtonClick = { favoritesPresenter.onFavoriteClick(it) }
        )
        favoritesRecyclerView = view.findViewById<RecyclerView>(R.id.favorites_recyclerZ).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
    }

    override fun showFavorites(entries: List<DictionaryEntry>) {
        favoritesAdapter.updateItems(entries.toMutableList())
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