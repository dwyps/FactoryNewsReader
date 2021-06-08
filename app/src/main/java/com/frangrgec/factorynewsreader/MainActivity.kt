package com.frangrgec.factorynewsreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.frangrgec.factorynewsreader.databinding.ActivityMainBinding
import com.frangrgec.factorynewsreader.ui.detailednews.DetailedNewsFragment
import com.frangrgec.factorynewsreader.ui.news.NewsFragment
import dagger.hilt.android.AndroidEntryPoint


private const val TAG_NEWS_FRAGMENT = "TAG_NEWS_FRAGMENT"
private const val TAG_DETAILED_NEWS_FRAGMENT = "TAG_DETAILED_NEWS_FRAGMENT"
private const val KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsFragment: NewsFragment
    private lateinit var detailedNewsFragment: DetailedNewsFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            newsFragment,
            detailedNewsFragment
        )

    private var selectedIndex = 0

    private val selectedFragment get() = fragments[selectedIndex]

    private fun selectFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()

        title = when (selectedFragment) {
            is NewsFragment -> "Factory News"
            is DetailedNewsFragment -> "Details"
            else -> ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            newsFragment = NewsFragment()
            detailedNewsFragment = DetailedNewsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, newsFragment, TAG_NEWS_FRAGMENT)
                .add(R.id.fragment_container, detailedNewsFragment, TAG_DETAILED_NEWS_FRAGMENT)
                .commit()
        } else {
            newsFragment =
                supportFragmentManager.findFragmentByTag(TAG_NEWS_FRAGMENT) as NewsFragment
            detailedNewsFragment =
                supportFragmentManager.findFragmentByTag(TAG_DETAILED_NEWS_FRAGMENT) as DetailedNewsFragment

            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0)
        }

        selectFragment(selectedFragment)

    }

    override fun onBackPressed() {

            super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex)
    }
}