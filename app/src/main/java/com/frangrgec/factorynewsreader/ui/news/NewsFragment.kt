package com.frangrgec.factorynewsreader.ui.news

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.frangrgec.factorynewsreader.R
import com.frangrgec.factorynewsreader.databinding.NewsFragmentBinding
import com.frangrgec.factorynewsreader.util.Resource
import com.frangrgec.factorynewsreader.util.exhaustive
import com.frangrgec.factorynewsreader.MainActivity
import com.frangrgec.factorynewsreader.shared.recyclerview.NewsArticleListAdapter
import com.frangrgec.factorynewsreader.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.news_fragment) {

    private val viewModel: NewsViewModel by viewModels()

    private var currentBinding: NewsFragmentBinding? = null
    private val binding get() = currentBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentBinding = NewsFragmentBinding.bind(view)

        setActionBar(getString(R.string.title_news))

        val newsArticleAdapter = NewsArticleListAdapter(
            onItemClick = { _, position->
                findNavController().navigate(NewsFragmentDirections.viewArticle(position))
            }
        )

        newsArticleAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            recyclerView.apply {
                adapter = newsArticleAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator?.changeDuration = TimeUnit.SECONDS.toMillis(1)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                viewModel.news.observe(viewLifecycleOwner, Observer {

                    val result = it ?:return@Observer

                    swipeRefreshLayout.isRefreshing = result is Resource.Loading

                    if (result.error != null && result.data.isNullOrEmpty()) {
                        displayErrorDialog(
                            result.error.localizedMessage ?: getString(R.string.unknown_error_occurred)
                        )
                    }
                    newsArticleAdapter.submitList(result.data) {
                        if (viewModel.pendingScrollToTopAfterRefresh) {
                            recyclerView.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }

                })
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }

            buttonRetry.setOnClickListener {
                viewModel.onManualRefresh()
            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                viewModel.events.collect { event ->

                    when(event) {
                        is NewsViewModel.Event.ShowErrorMessage -> {
                            displayErrorDialog(
                                event.error.localizedMessage ?: getString(R.string.unknown_error_occurred)
                            )
                        }
                    }.exhaustive
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

    private fun setActionBar(title: String?) {

        (requireActivity() as MainActivity).supportActionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun displayErrorDialog(message: String) {

        MaterialDialog(requireContext()).show {
            title(R.string.could_not_refresh)
            message(text = message)
            positiveButton(R.string.retry) { dialog ->
                viewModel.onManualRefresh()
                dialog.cancel()
            }
            negativeButton(R.string.cancel) { dialog ->
                dialog.cancel()
            }
        }
    }
}