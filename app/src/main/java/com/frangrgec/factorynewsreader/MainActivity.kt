package com.frangrgec.factorynewsreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.frangrgec.factorynewsreader.databinding.ActivityMainBinding
import com.frangrgec.factorynewsreader.ui.detailednews.DetailedNewsFragment
import com.frangrgec.factorynewsreader.ui.news.NewsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}