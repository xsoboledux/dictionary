package ru.xsobolx.dictionary.data.db.base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.xsobolx.dictionary.data.db.translation.dao.TranslationDAO
import ru.xsobolx.dictionary.data.db.translation.model.DictionaryDBModel
import ru.xsobolx.dictionary.data.db.translation.typeconverter.TranslationTypeConverters

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "dictionary.db"

@Database(entities = [DictionaryDBModel::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(TranslationTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun translationDao(): TranslationDAO
}
