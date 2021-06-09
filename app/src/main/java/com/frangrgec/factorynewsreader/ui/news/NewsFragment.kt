package com.frangrgec.factorynewsreader.ui.news

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.frangrgec.factorynewsreader.R
import com.frangrgec.factorynewsreader.databinding.NewsFragmentBinding
import com.frangrgec.factorynewsreader.shared.NewsArticleListAdapter
import com.frangrgec.factorynewsreader.util.Resource
import com.frangrgec.factorynewsreader.util.exhaustive
import com.frangrgec.factorynewsreader.util.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        val newsArticleAdapter = NewsArticleListAdapter(
            onItemClick = { article ->
                //TODO ViewPager Open Article
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
                viewModel.news.collect {

                    val result = it ?: return@collect

                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()

                    if (result.error != null && result.data.isNullOrEmpty()) {

                        val message = result.error.localizedMessage ?: getString(R.string.unknown_error_occurred)

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

                    newsArticleAdapter.submitList(result.data) {
                        if (viewModel.pendingScrollToTopAfterRefresh) {
                            recyclerView.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
                }
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
                            val message = event.error.localizedMessage ?: getString(R.string.unknown_error_occurred)

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

}