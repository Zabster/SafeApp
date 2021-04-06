package ru.zabster.safepassapp.app

import android.app.Application
import com.google.android.material.resources.TextAppearanceConfig
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.DaggerAppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.di.impls.shared.SPKeys
import ru.zabster.safepassapp.di.modules.DataBaseModule
import ru.zabster.safepassapp.di.modules.ResourceModule
import ru.zabster.safepassapp.di.modules.SharedPreferenceModule
import ru.zabster.safepassapp.utils.theme.AppTheme

class App : Application() {

    /**
     * Dagger app components
     */
    lateinit var appComponents: AppComponents
        private set

    override fun onCreate() {
        super.onCreate()
        application = this
        appComponents = DaggerAppComponents.builder()
            .sharedPreferenceModule(SharedPreferenceModule(this))
            .resourceModule(ResourceModule(this))
            .build()
        TextAppearanceConfig.setShouldLoadFontSynchronously(true)
        AppTheme.updateTheme(appComponents.getSharedPreference().getBoolean(SPKeys.KEY_DARK_MODE))
    }

    companion object {

        private lateinit var application: App

        /**
         * Get app instance
         *
         * @return app instance
         */
        fun getApplication() = application
    }
}