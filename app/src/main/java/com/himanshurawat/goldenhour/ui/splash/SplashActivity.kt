package com.himanshurawat.goldenhour.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.himanshurawat.goldenhour.R
import com.himanshurawat.goldenhour.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var progressStatus = 0

        val handler = Handler()


        Thread(Runnable {
            while (progressStatus < 100) {
                progressStatus += 1
                // Update the progress bar and display the
                //current value in the text view
                handler.post { progress_bar.progress = progressStatus }
                try {
                    // Sleep for 50 milliseconds.
                    Thread.sleep(20)
                    if (progressStatus == 100) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }

            }
        }).start()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}