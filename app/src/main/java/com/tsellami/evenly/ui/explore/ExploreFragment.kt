package com.tsellami.evenly.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tsellami.evenly.R
import com.tsellami.evenly.databinding.ExploreFragmentBinding
import com.tsellami.evenly.repository.rooms.models.Venue
import com.tsellami.evenly.ui.adapter.VenueAdapter
import com.tsellami.evenly.ui.adapter.VenueLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.explore_fragment), VenueAdapter.OnItemListener {

    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var binding: ExploreFragmentBinding
    private lateinit var venueAdapter: VenueAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ExploreFragmentBinding.bind(view)
        initAdapter()
        binding.swipeLayout.setOnRefreshListener {
            viewModel.deleteCachedData()
            venueAdapter.refresh()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            venueAdapter.loadStateFlow.collect { loadState ->
                when (loadState.mediator?.refresh) {
                    is LoadState.NotLoading -> {
                        setNotLoading(loadState)
                    }
                    is LoadState.Error -> {
                        setError()
                    }
                    is LoadState.Loading -> {
                        setLoading()
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        venueAdapter = VenueAdapter(this)
        venueAdapter.withLoadStateHeaderAndFooter(
            header = VenueLoadStateAdapter { venueAdapter.retry() },
            footer = VenueLoadStateAdapter { venueAdapter.retry() }
        )
        binding.recommendationsRecyclerView.apply {
            itemAnimator = null
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.venues.observe(viewLifecycleOwner, {
            viewLifecycleOwner.lifecycleScope.launch {
                venueAdapter.submitData(it)
            }
        })
    }

    private fun setError() {
        Snackbar.make(
            requireView(),
            requireContext().getText(R.string.error_occurred),
            Snackbar.LENGTH_SHORT
        ).show()
        binding.apply {
            loadingView.visibility = View.GONE
            infoMessage.visibility = View.VISIBLE
            mainLayout.visibility = View.VISIBLE
            swipeLayout.isRefreshing = false
            infoMessage.text = requireContext().getText(R.string.error_occurred)
            infoMessage.setCompoundDrawables(null, null, null, null)
        }
    }

    private fun setNotLoading(loadState: CombinedLoadStates) {
        binding.apply {
            mainLayout.visibility = View.VISIBLE
            loadingView.visibility = View.GONE
            swipeLayout.isRefreshing = false
            recommendationsRecyclerView.isVisible = venueAdapter.itemCount > 0
            val isEmpty = venueAdapter.itemCount < 1
                    && loadState.prepend.endOfPaginationReached
                    && loadState.source.append.endOfPaginationReached
            infoMessage.isVisible = isEmpty
        }
    }

    private fun setLoading() {
        binding.apply {
            mainLayout.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
        }
    }

    override fun onClick(venue: Venue) {
        val action = ExploreFragmentDirections.actionExploreToDetailedVenueFragment(
            id = venue.id,
            name = venue.name,
            categoryName = venue.categoryName,
            address = venue.address,
            distance = venue.distance,
        )
        findNavController().navigate(action)
    }
}