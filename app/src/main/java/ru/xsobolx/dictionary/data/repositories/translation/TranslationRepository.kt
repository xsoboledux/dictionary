package ru.xsobolx.dictionary.data.repositories.translation

import io.reactivex.Single
import ru.xsobolx.dictionary.domain.translation.model.DictionaryEntry

interface TranslationRepository {

    fun getTranslation(value: String) : Single<DictionaryEntry>
}