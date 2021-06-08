package com.frangrgec.factorynewsreader.ui.detailednews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frangrgec.factorynewsreader.R

class DetailedNewsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailedNewsFragment()
    }

    private lateinit var viewModel: DetailedNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailedNewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}