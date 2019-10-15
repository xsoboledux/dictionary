package ru.xsobolx.dictionary.presentation.phrasebook.view

import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry
import ru.xsobolx.dictionary.presentation.base.BaseMvpView

interface PhraseBookView : BaseMvpView {

    fun showEntries(entries: List<DictionaryEntry>)
}