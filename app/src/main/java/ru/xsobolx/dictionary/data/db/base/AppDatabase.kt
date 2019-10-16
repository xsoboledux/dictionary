package ru.xsobolx.dictionary.data.db.base

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "dictionary.db"

@Database(entities = [DictionaryDBModel::class], version = DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun translationDao(): TranslationDAO
}
