package com.frangrgec.factorynewsreader.ui.detailednews


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.frangrgec.factorynewsreader.MainActivity
import com.frangrgec.factorynewsreader.R
import com.frangrgec.factorynewsreader.data.NewsArticle
import com.frangrgec.factorynewsreader.databinding.DetailedNewsFragmentBinding
import com.frangrgec.factorynewsreader.databinding.NewsFragmentBinding
import com.frangrgec.factorynewsreader.shared.viewpager.ViewPagerAdapter
import com.frangrgec.factorynewsreader.ui.news.NewsViewModel
import com.frangrgec.factorynewsreader.util.REQUEST_DATA
import com.frangrgec.factorynewsreader.util.REQUEST_KEY
import com.frangrgec.factorynewsreader.util.Resource
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DetailedNewsFragment : Fragment(R.layout.detailed_news_fragment) {

    private val viewModel: NewsViewModel by viewModels()

    private var currentBinding: DetailedNewsFragmentBinding? = null
    private val binding get() = currentBinding!!

    private val args: DetailedNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentBinding = DetailedNewsFragmentBinding.bind(view)

        val articlesAdapter = ViewPagerAdapter()

        articlesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        setFragmentResultListener(REQUEST_KEY) { key, bundle ->

            val currentArticle = Gson().fromJson(bundle.getString(REQUEST_DATA), NewsArticle::class.java)

            (requireActivity() as MainActivity).supportActionBar?.apply {
                title = currentArticle.title
                setDisplayHomeAsUpEnabled(true)
            }
        }

        binding.apply {

            viewPager.apply {
                adapter = articlesAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                viewModel.news.collect {

                    val result = it ?: return@collect

                    if (!result.data.isNullOrEmpty()) {

                        articlesAdapter.submitList(result.data) {
                            if (viewModel.pendingScrollToTopAfterRefresh) {
                                viewModel.pendingScrollToTopAfterRefresh = false
                            }
                            viewPager.currentItem = args.articleIndex
                        }

                        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)

                                (requireActivity() as MainActivity).supportActionBar?.apply {
                                    title = result.data[position].title
                                    setDisplayHomeAsUpEnabled(true)
                                }
                            }
                        })
                    }
                }
            }


        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentBinding = null
    }
}