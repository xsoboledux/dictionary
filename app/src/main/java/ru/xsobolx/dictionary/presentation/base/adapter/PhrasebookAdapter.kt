package ru.xsobolx.dictionary.presentation.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

class PhrasebookAdapter(
    var items: MutableList<DictionaryEntry> = mutableListOf(),
    private val onFavoriteButtonClick: (DictionaryEntry) -> Unit
) : RecyclerView.Adapter<PhrasebookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhrasebookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dictionary_entry_list_item, parent, false)
        return PhrasebookViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PhrasebookViewHolder, position: Int) {
        holder.onAttach(items[position], onFavoriteButtonClick)
    }

    fun updateItems(items: MutableList<DictionaryEntry>) {
        this.items = items
        notifyDataSetChanged()
    }
}