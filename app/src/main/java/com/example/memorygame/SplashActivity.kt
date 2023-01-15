// COMP 4521    //  CHENG On Kiu       20516055       okcheng@connect.ust.hk

package com.example.memorygame

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        // If user account exists, start Menu Activity, else Login Activity
        if (Build.VERSION.SDK_INT < 30){
            Handler().postDelayed({
                if(user != null){
                    val menuIntent = Intent(this, MenuActivity::class.java)
                    startActivity(menuIntent)
                    finish()
                }else{
                    val signInIntent = Intent(this, SignInActivity::class.java)
                    startActivity(signInIntent)
                    finish()
                }
            },2000)
        }else{

            Handler(Looper.getMainLooper()).postDelayed({
                if(user != null){
                    val menuIntent = Intent(this, MenuActivity::class.java)
                    startActivity(menuIntent)
                    finish()
                }else{
                    val signInIntent = Intent(this, SignInActivity::class.java)
                    startActivity(signInIntent)
                    finish()
                }
            },2000)
        }
    }
}
