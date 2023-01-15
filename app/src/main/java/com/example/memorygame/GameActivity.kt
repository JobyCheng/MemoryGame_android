// COMP 4521    //  CHENG On Kiu       20516055       okcheng@connect.ust.hk

package com.example.memorygame

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children


class GameActivity : AppCompatActivity(){

    //load animal images
    private val drawables: MutableList<Int> = mutableListOf(
        R.drawable.bird,
        R.drawable.coala,
        R.drawable.crocodile,
        R.drawable.dog,
        R.drawable.duck,
        R.drawable.eagle,
        R.drawable.fish,
        R.drawable.fox,
        R.drawable.parrot,
        R.drawable.penguin,
        R.drawable.sheep,
        R.drawable.squirrel,
        R.drawable.tiger,
        R.drawable.wolf
    )

    lateinit var grid: GridLayout
    var numOfCards = 0
    lateinit var cardList: IntArray
    var numOfImages = drawables.size
    private var level = 0
    var tries = 0
    var pass = true
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.hide()

        setCards()
    }

    private fun setCards() {
        level = intent.getIntExtra("GAME_LEVEL_KEY", 1) // 1 or 2 or 3
        grid = findViewById<View>(R.id.grid) as GridLayout
        numOfCards = level * 8 // 8 or 16 or 24
        when (level) {
            1 -> {
                grid.rowCount = 4
                grid.columnCount = 2
            }
            2 -> {
                grid.rowCount = 4
                grid.columnCount = 4
            }
            else -> {
                grid.rowCount = 6
                grid.columnCount = 4
            }
        }
        // Embed the image views in each location
        for (i in 0 until numOfCards) {
            grid.addView(ImageView(this), i)
        }
        shuffleCards()
    }

    private fun shuffleCards() {
        val numOfImage = MutableList(numOfImages) { i -> i * 1 } //get animal image name list
        numOfImage.shuffle()
        val imageList = numOfImage.toList()
        val imageUsed = imageList.take(numOfCards / 2)       //get amount of image names needed
        val temp = imageUsed.toMutableList()
        temp += temp                                             //double the list
        temp.shuffle()
        cardList = temp.toIntArray()                            //the card list used in the game

        startGame()
    }

    private fun startGame() {
        lateinit var lastCard: ImageView
        lateinit var lastCard2: ImageView
        var clicks = -1
        var turnOver = false
        var triesText = findViewById<View>(R.id.tvTries) as TextView
        var timeText = findViewById<View>(R.id.tvTime) as TextView
        var mode = intent.getIntExtra("GAME_MODE", 1)

        //set timer
        if(mode == 2){
            when (level) {
                1 -> {
                    timeText.text = "Time: 30"
                    timer = object : CountDownTimer(30000, 1000) {
                        override fun onFinish() {
                            pass = false
                            results()
                        }
                        override fun onTick(mills: Long) {
                            timeText.text = "Time: "+ mills/1000
                        }
                    }
                }
                2 -> {
                    timeText.text = "Time: 50"
                    timer = object : CountDownTimer(50000, 1000) {
                        override fun onFinish() {
                            pass = false
                            results()
                        }
                        override fun onTick(mills: Long) {
                            timeText.text = "Time: "+ mills/1000
                        }
                    }
                }
                3 -> {
                    timeText.text = "Time: 80"
                    timer = object : CountDownTimer(80000, 1000) {
                        override fun onFinish() {
                            pass = false
                            results()
                        }
                        override fun onTick(mills: Long) {
                            timeText.text = "Time: "+ mills/1000
                        }
                    }
                }
            }

        }

        // Put the images in the GridLayout.
        for (i in 0 until numOfCards) {
            val v: ImageView = grid.getChildAt(i) as ImageView
            when (level) {
                1 -> {
                    v.layoutParams.height = 300
                    v.layoutParams.width = 300
                }
                else -> {
                    v.layoutParams.height = 250
                    v.layoutParams.width = 250
                }
            }
            v.setImageResource(R.drawable.magniglass)
            //v.setBackgroundResource(drawables[cardList[i]])
            v.tag = drawables[cardList[i]]
            v.setOnClickListener(){
                if(clicks > -1 && v == lastCard){
                    Toast.makeText(this, "Repeated Card!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                when(clicks){
                    -1 -> {
                        if(mode == 2) timer.start()
                        v.setImageResource(v.tag as Int)
                        lastCard = v
                        clicks = 1
                    }
                    0 -> {
                        if (turnOver) {
                            lastCard.setImageResource(R.drawable.magniglass)
                            lastCard2.setImageResource(R.drawable.magniglass)
                            turnOver = false
                        } else {
                            lastCard.alpha = 0.0F
                            lastCard2.alpha = 0.0F
                        }
                        v.setImageResource(v.tag as Int)
                        lastCard = v
                        clicks++
                    }
                    else -> {
                        v.setImageResource(v.tag as Int)
                        if(lastCard.tag == v.tag){
                            lastCard.isClickable = false
                            v.isClickable = false
                        }else{
                            turnOver = true
                        }
                        lastCard2 = lastCard
                        lastCard = v
                        clicks = 0
                        tries++
                        triesText.text = getString(R.string.tries, tries)
                    }
                }
                for (i in grid.children){
                    if(i.isClickable){
                        return@setOnClickListener
                    }
                }
                if(mode == 2) timer.cancel()
                results()
            }
        }
    }

    private fun results() {
        val builder = AlertDialog.Builder(this)

        if(pass){
            builder.setTitle("Bravo!")
            val inflater = LayoutInflater.from(this)
            val v: View = inflater.inflate(R.layout.congrats_layout, null)
            builder.setView(v)
            builder.setMessage("Number of tries: $tries")
        }else{
            pass = true
            builder.setTitle("Oh...")
            val inflater = LayoutInflater.from(this)
            val v: View = inflater.inflate(R.layout.sad_layout, null)
            builder.setView(v)
            builder.setMessage("Try Harder Next Time!")
        }

        //set continue button
        builder.setPositiveButton("Continue") { _, _ ->
            // User clicked Update Now button
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        //set retry button
        builder.setNegativeButton("Retry") { _, _ ->
            grid.removeAllViews()
            setCards()
        }
        builder.show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        var mode = intent.getIntExtra("GAME_MODE", 1)
        if(mode == 2) timer.cancel()
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}