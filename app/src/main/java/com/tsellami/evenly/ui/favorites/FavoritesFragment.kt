package com.tsellami.evenly.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsellami.evenly.R
import com.tsellami.evenly.databinding.FavoritesFragmentBinding
import com.tsellami.evenly.repository.rooms.models.Venue
import com.tsellami.evenly.ui.adapter.VenueAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.favorites_fragment), VenueAdapter.OnItemListener {

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FavoritesFragmentBinding
    private lateinit var venueAdapter: VenueAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FavoritesFragmentBinding.bind(view)
        venueAdapter = VenueAdapter(this)
        initAdapter()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            venueAdapter.loadStateFlow.collect { loadState ->
                if (loadState.append is LoadState.NotLoading) {
                    binding.apply {
                        favoritesRecyclerView.isVisible = venueAdapter.itemCount > 0
                        val isEmpty = venueAdapter.itemCount < 1
                                && loadState.prepend.endOfPaginationReached
                                && loadState.source.append.endOfPaginationReached
                        empty.isVisible = isEmpty
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        binding.favoritesRecyclerView.apply {
            itemAnimator = null
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.favorites.observe(viewLifecycleOwner, {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                venueAdapter.submitData(it)
            }
        })
    }

    override fun onClick(venue: Venue) {
        val action = FavoritesFragmentDirections.actionFavoritesToDetailedVenueFragment(
            id = venue.id,
            name = venue.name,
            categoryName = venue.categoryName,
            address = venue.address,
            distance = venue.distance,
        )
        findNavController().navigate(action)
    }

}