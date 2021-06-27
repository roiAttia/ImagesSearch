package roiattia.com.imagessearch.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesManager @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String): String? {
        return prefs.getString(key, defValue)
    }

}