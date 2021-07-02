package com.tsellami.evenly.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tsellami.evenly.R
import com.tsellami.evenly.databinding.ExploreFragmentBinding
import com.tsellami.evenly.rooms.Venue
import com.tsellami.evenly.ui.adapter.VenueAdapter
import com.tsellami.evenly.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.explore_fragment), VenueAdapter.OnItemListener {

    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var binding: ExploreFragmentBinding
    private lateinit var venueAdapter: VenueAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ExploreFragmentBinding.bind(view)
        venueAdapter = VenueAdapter(this)
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

        binding.swipeLayout.setOnRefreshListener {
            viewModel.deleteCachedData()
            venueAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            venueAdapter.loadStateFlow.collect { loadState ->
                when (loadState.mediator?.refresh) {
                    is LoadState.NotLoading -> {
                        binding.apply {
                            mainLayout.visibility = View.VISIBLE
                            loading.visibility = View.GONE
                            swipeLayout.isRefreshing = false
                        }
                    }
                    is LoadState.Error -> {
                        Snackbar.make(requireView(), "An error occurred. Please try again!", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.apply {
                            mainLayout.visibility = View.GONE
                            loading.visibility = View.VISIBLE
                        }
                    }
                }
            }
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