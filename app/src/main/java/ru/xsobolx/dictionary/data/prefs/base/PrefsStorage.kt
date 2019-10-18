package ru.xsobolx.dictionary.data.prefs.base

import android.content.Context
import android.content.SharedPreferences
import ru.xsobolx.dictionary.di.app.AppContext
import javax.inject.Inject

private const val SHARED_PREFERENCES_FILE_KEY = "ru.xsobolx.dictionary.PREFERENCE_FILE_KEY"

interface PrefsStorage {

    fun saveString(key: String, string: String)

    fun getString(key: String): String?

    fun saveInt(integer: Int, key: String)

    fun getInt(key: String): Int?

    class Impl
    @Inject constructor(
        @AppContext private val context: Context
    ) : PrefsStorage {
        private var sharedPref: SharedPreferences

        init {
            sharedPref = context.getSharedPreferences(
                SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE
            )
        }

        override fun saveString(key: String, string: String) {
            with(sharedPref.edit()) {
                putString(key, string)
                commit()
            }
        }

        override fun getString(key: String): String? {
            return sharedPref.getString(key, "")
        }

        override fun saveInt(integer: Int, key: String) {
            with(sharedPref.edit()) {
                putInt(key, integer)
                commit()
            }
        }

        override fun getInt(key: String): Int? {
            return sharedPref.getInt(key, 0)
        }
    }
}