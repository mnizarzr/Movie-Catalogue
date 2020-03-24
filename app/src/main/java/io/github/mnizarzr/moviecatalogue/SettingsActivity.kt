package io.github.mnizarzr.moviecatalogue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.github.mnizarzr.moviecatalogue.receiver.DailyReminder
import io.github.mnizarzr.moviecatalogue.receiver.ReleaseReminder
import io.github.mnizarzr.moviecatalogue.utils.showToast

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        companion object {
            private const val KEY_DAILY_REMINDER = "key_daily_reminder"
            private const val KEY_RELEASE_REMINDER = "key_release_reminder"
        }

        private lateinit var dailyReminder: DailyReminder
        private lateinit var releaseReminder: ReleaseReminder

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            dailyReminder = DailyReminder()
            releaseReminder = ReleaseReminder()

            val switchDailyReminder = findPreference<SwitchPreferenceCompat>(KEY_DAILY_REMINDER)
            val switchReleaseReminder = findPreference<SwitchPreferenceCompat>(KEY_RELEASE_REMINDER)

            switchDailyReminder?.onPreferenceChangeListener = this
            switchReleaseReminder?.onPreferenceChangeListener = this

        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            val disabled = newValue as Boolean
            when (preference?.key) {
                KEY_DAILY_REMINDER -> {
                    if (disabled) {
                        context?.let {
                            dailyReminder.setReminder(it)
                            if (dailyReminder.isSet(it)) it.showToast(it.resources.getString(R.string.enabled_daily))
                        }
                    } else {
                        context?.let {
                            dailyReminder.cancelReminder(it)
                            if (!dailyReminder.isSet(it)) it.showToast(it.resources.getString(R.string.disabled_daily))
                        }
                    }
                }
                KEY_RELEASE_REMINDER -> {
                    if (disabled) {
                        context?.let {
                            releaseReminder.setReminder(it)
                            if (releaseReminder.isSet(it)) it.showToast(it.resources.getString(R.string.enabled_release))
                        }
                    } else {
                        context?.let {
                            releaseReminder.cancelReminder(it)
                            if (!releaseReminder.isSet(it)) it.showToast(it.resources.getString(R.string.disabled_release))
                        }
                    }
                }
            }
            return true
        }

    }
}