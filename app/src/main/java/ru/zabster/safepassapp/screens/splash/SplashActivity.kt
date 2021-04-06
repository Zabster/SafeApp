package ru.zabster.safepassapp.screens.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.screens.login.presentation.LoginActivity

/**
 * Splash screen
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var countTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initTimer()
    }

    override fun onResume() {
        super.onResume()
        countTimer.start()
    }

    override fun onPause() {
        countTimer.cancel()
        super.onPause()
    }

    private fun initTimer() {
        countTimer = object : CountDownTimer(START_DELAY, START_DELAY) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                startLoginScreen()
            }
        }
    }

    private fun startLoginScreen() {
        LoginActivity.startLoginScreen(this)
    }

    companion object {

        private const val START_DELAY = 1000L
    }
}