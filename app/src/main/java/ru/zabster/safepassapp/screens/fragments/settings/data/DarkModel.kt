package ru.zabster.safepassapp.screens.fragments.settings.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import ru.zabster.safepassapp.di.impls.shared.SPKeys
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.utils.theme.AppTheme

/**
 * Model for set dark state for app using two-way binding
 *
 * @param sharedPreferenceHelper local storage
 */
class DarkModel(
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : BaseObservable() {

    /**
     * is dark mod or not
     */
    var isDark: Boolean = sharedPreferenceHelper.getBoolean(SPKeys.KEY_DARK_MODE)
        @Bindable get
        set(value) {
            if (field != value) {
                field = value
                sharedPreferenceHelper.putBoolean(SPKeys.KEY_DARK_MODE, value)
                AppTheme.updateTheme(value)
                notifyPropertyChanged(BR.dark)
            }
        }
}