package ru.xsobolx.dictionary.data.repositories.translation

import io.reactivex.Completable
import io.reactivex.Single
import ru.xsobolx.dictionary.data.prefs.base.PrefsStorage
import ru.xsobolx.dictionary.domain.translation.model.Language
import ru.xsobolx.dictionary.domain.translation.model.LanguageEntity
import ru.xsobolx.dictionary.domain.translation.model.TranslationDirection
import java.lang.IllegalArgumentException
import javax.inject.Inject

interface LanguageRepository {

    companion object {
        const val FROM_LANGUAGE_KEY = "FROM_LANGUAGE_PREF_KEY"
        const val TO_LANGUAGE_KEY = "TO_LANGUAGE_PREF_KEY"
    }

    fun getLanguage(direction: TranslationDirection): Single<LanguageEntity>

    fun setLanguage(languageEntity: LanguageEntity): Completable

    fun getLanguages(): Single<Set<Language>>

    class Impl
    @Inject constructor(private val prefsStorage: PrefsStorage) : LanguageRepository {
        override fun getLanguages(): Single<Set<Language>> {
            return Single.just(Language.values().toSet())
        }

        override fun setLanguage(languageEntity: LanguageEntity): Completable {
            return Completable.fromAction {
                val key = mapDirectionToKey(languageEntity.translationDirection)
                prefsStorage.saveString(key = key, string = languageEntity.language.lang)
            }
        }

        private fun mapDirectionToKey(direction: TranslationDirection): String {
            return when (direction) {
                TranslationDirection.FROM -> FROM_LANGUAGE_KEY
                TranslationDirection.TO -> TO_LANGUAGE_KEY
            }
        }

        override fun getLanguage(direction: TranslationDirection): Single<LanguageEntity> {
            return Single.fromCallable {
                val key = mapDirectionToKey(direction)
                val lang = prefsStorage.getString(key) as String
                val language = try {
                    Language.valueOf(lang.toUpperCase())
                } catch (err: IllegalArgumentException) {
                    Language.EN
                }
                LanguageEntity(language, direction)
            }
        }
    }
}