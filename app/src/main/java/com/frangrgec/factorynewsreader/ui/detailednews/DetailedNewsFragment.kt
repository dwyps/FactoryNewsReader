package com.frangrgec.factorynewsreader.ui.detailednews


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.frangrgec.factorynewsreader.MainActivity
import com.frangrgec.factorynewsreader.R
import com.frangrgec.factorynewsreader.databinding.DetailedNewsFragmentBinding
import com.frangrgec.factorynewsreader.shared.viewpager.ViewPagerAdapter
import com.frangrgec.factorynewsreader.ui.news.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


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

        var currentPage = args.articleIndex

        binding.apply {

            viewPager.apply {
                adapter = articlesAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                viewModel.news.observe(viewLifecycleOwner, Observer {

                    val result = it ?: return@Observer

                    if (!result.data.isNullOrEmpty()) {

                        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)

                                currentPage = position
                                setActionBar(result.data[currentPage].title)

                            }
                        })

                        articlesAdapter.submitList(result.data) {
                            if (viewModel.pendingScrollToTopAfterRefresh)
                                viewModel.pendingScrollToTopAfterRefresh = false

                            setActionBar(result.data[currentPage].title)
                        }

                        viewPager.currentItem = currentPage
                    }
                })
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

    private fun setActionBar(title: String?) {

        (requireActivity() as MainActivity).supportActionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(true)
        }
    }
}