package com.wayproject.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wayproject.bwamov.R
import com.wayproject.bwamov.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_home2.setOnClickListener {
            finishAffinity()

            var intent = Intent (this@OnboardingThreeActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}