package ru.xsobolx.dictionary.presentation.base.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

class PhrasebookViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private var word: TextView
    private var translation: TextView
    private var favoriteButton: ImageButton
    private var language: TextView

    init {
        word = itemView.findViewById(R.id.word_to_translate_text_view)
        translation = itemView.findViewById(R.id.translation_text_view)
        favoriteButton = itemView.findViewById(R.id.favorite_button)
        language = itemView.findViewById(R.id.language_text_view)
    }

    fun onAttach(dictionaryEntry: DictionaryEntry,
        onFavoriteButtonClick: (DictionaryEntry) -> Unit) {
        word.text = dictionaryEntry.word
        translation.text = dictionaryEntry.translation
        language.text = "${dictionaryEntry.fromLanguage}-${dictionaryEntry.toLanguage}"
        val imagage = if (dictionaryEntry.isFavorite) {
            R.drawable.ic_favorite_button_true
        } else {
            R.drawable.ic_favorite_button_image_false
        }
        favoriteButton.setImageDrawable(itemView.context.getDrawable(imagage))
        favoriteButton.setOnClickListener {
            onFavoriteButtonClick(dictionaryEntry)
        }
    }
}