package ru.xsobolx.dictionary.presentation.main.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.xsobolx.dictionary.R
import ru.xsobolx.dictionary.presentation.favorites.ui.FavoritesFragment
import ru.xsobolx.dictionary.presentation.phrasebook.ui.PhrasebookFragment
import ru.xsobolx.dictionary.presentation.translation.ui.TranslationFragment

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomNavigationBar: BottomNavigationView

    private var translateFragment: TranslationFragment? = null
    private var phrasebookFragment: PhrasebookFragment? = null
    private var favoritesFragment: FavoritesFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationBar = findViewById(R.id.bottom_navigation)
        fragmentContainer = findViewById(R.id.fragment_container)

        bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_favorites -> {
                    if (favoritesFragment == null) {
                        favoritesFragment = FavoritesFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, favoritesFragment!!)
                        .commit()
                }
                R.id.action_translate -> {
                    if (translateFragment == null) {
                        translateFragment = TranslationFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, translateFragment!!)
                        .commit()
                }
                R.id.action_recents -> {
                    if (phrasebookFragment == null) {
                        phrasebookFragment = PhrasebookFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, phrasebookFragment!!)
                        .commit()
                }
            }
            true
        }
        bottomNavigationBar.selectedItemId = R.id.action_translate
    }

}