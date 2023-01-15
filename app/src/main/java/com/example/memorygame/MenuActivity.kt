// COMP 4521    //  CHENG On Kiu       20516055       okcheng@connect.ust.hk

package com.example.memorygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {
    var level = 0
    var gameMode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()

        val difficulty = resources.getStringArray(R.array.difficulty)
        val mode = resources.getStringArray(R.array.mode)
        // access the spinner
        val spinnerDifficulty = findViewById<Spinner>(R.id.spinnerDifficulty)
        if (spinnerDifficulty != null) {
            val adapter = ArrayAdapter(this, R.layout.layout_spinner, difficulty)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDifficulty.adapter = adapter

            spinnerDifficulty.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    level = position
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        val spinnerMode = findViewById<Spinner>(R.id.spinnerMode)
        if (spinnerMode != null) {
            val adapter = ArrayAdapter(this, R.layout.layout_spinner, mode)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMode.adapter = adapter

            spinnerMode.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    gameMode = position
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    fun selectGame(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        when (level) {
            0 -> intent.putExtra("GAME_LEVEL_KEY", 1) //Easy
            1 -> intent.putExtra("GAME_LEVEL_KEY", 2) //Medium
            2 -> intent.putExtra("GAME_LEVEL_KEY", 3) //Hard
        }
        when (gameMode) {
            0 -> intent.putExtra("GAME_MODE", 1) //Basic
            1 -> intent.putExtra("GAME_MODE", 2) //Timed
        }
        startActivity(intent)
        finish()
    }

    fun viewProfile(v: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
