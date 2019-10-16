package ru.xsobolx.dictionary.data.db.translation.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import ru.xsobolx.dictionary.data.db.translation.model.DICTIONARY_ENTRIES_TABLE_NAME
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel

@Dao
interface TranslationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDictionaryEntry(entry: DictionaryDBModel) : Completable

    @Update
    fun updateDictionaryEntry(entry: DictionaryDBModel) : Completable

    @Query("SELECT * FROM $DICTIONARY_ENTRIES_TABLE_NAME")
    fun loadAllDictionaryEntries() : Single<List<DictionaryDBModel>>

    @Query("SELECT * FROM $DICTIONARY_ENTRIES_TABLE_NAME WHERE word LIKE :search OR translation LIKE :search")
    fun findTranslation(search: String) : Maybe<List<DictionaryDBModel>>
}