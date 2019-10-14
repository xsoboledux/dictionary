package ru.xsobolx.dictionary.data.db.translation.typeconverter

import androidx.room.TypeConverter
import ru.xsobolx.dictionary.domain.translation.model.Language

class TranslationTypeConverters {
    @TypeConverter
    fun toLanguage(value: String): Language = Language.valueOf(value)

    @TypeConverter
    fun fromLanguage(language: Language) = language.lang
}