// COMP 4521    //  CHENG On Kiu       20516055       okcheng@connect.ust.hk

package com.example.memorygame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val id = findViewById<TextView>(R.id.id_txt)
        val name = findViewById<TextView>(R.id.name_txt)
        val email = findViewById<TextView>(R.id.email_txt)
        val profilePic = findViewById<CircleImageView>(R.id.profile_image)

        id.text = currentUser?.uid
        name.text = currentUser?.displayName
        email.text = currentUser?.email

        Glide.with(this).load(currentUser?.photoUrl).into(profilePic)
    }

    fun signOut(v: View) {
        mAuth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}