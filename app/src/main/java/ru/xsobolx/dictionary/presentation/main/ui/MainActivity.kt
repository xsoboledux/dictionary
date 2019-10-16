package ru.xsobolx.dictionary.presentation.main.ui

import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.xsobolx.dictionary.R

class MainActivity : Activity() {
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationBar = findViewById(R.id.bottom_navigation)
        bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_favorites -> true
                R.id.action_translate -> true
                R.id.action_recents -> true
                else -> true
            }
        }
    }

}