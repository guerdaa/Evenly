package com.tsellami.evenly.ui.detailedVenue

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tsellami.evenly.R
import com.tsellami.evenly.databinding.DetailedVenueFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailedVenueFragment : Fragment(R.layout.detailed_venue_fragment) {

    private val viewModel: DetailedVenueViewModel by viewModels()
    private val args: DetailedVenueFragmentArgs by navArgs()
    private lateinit var binding: DetailedVenueFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailedVenueFragmentBinding.bind(view)
        bindDetails()
        bindGlobalDetails()
        setFavorite()
        viewModel.retrieveDetails(args.id)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.resource.collect { event ->
                when (event) {
                    is DetailedVenueViewModel.RetrievingEvent.Loading -> {
                        binding.apply {
                            mainLayout.visibility = View.GONE
                            loadingView.visibility = View.VISIBLE
                        }
                    }
                    is DetailedVenueViewModel.RetrievingEvent.Success -> {
                        binding.apply {
                            mainLayout.visibility = View.VISIBLE
                            loadingView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun bindGlobalDetails() {
        binding.apply {
            name.text = args.name
            address.text = args.address
            categoryName.text = args.categoryName
            distance.text = "${args.distance}m"
        }
    }

    private fun bindDetails() {
        viewModel.details.observe(viewLifecycleOwner, { details ->
            binding.apply {
                details?.let {
                    price.text = details.price ?: setNotAvailable()
                    rating.text = "${details.rating} / 10"
                    openingHours.text = details.openingHours ?: setNotAvailable()
                    Glide.with(requireContext()).load(details.thumbnail).centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(thumbnail)
                    shareButton.setOnClickListener {
                        shareCanonicalUrl(details.canonicalUrl)
                    }
                    maps.setOnClickListener {
                        setGoogleMapsCoordinates(details.lat, details.lng, args.name)
                    }
                }
            }
        })
    }

    private fun setFavorite() {
        binding.favorites.setOnClickListener {
            viewModel.onFavoriteClickListener(args.id)
        }
        viewModel.isFavorite.observe(viewLifecycleOwner, { isFavorite ->
            binding.apply {
                if (isFavorite) {
                    favorites.setImageResource(R.drawable.ic_favorite)
                } else {
                    favorites.setImageResource(R.drawable.ic_not_favorite)
                }
            }
        })
    }

    private fun setNotAvailable() = requireContext().getText(R.string.not_available)

    private fun shareCanonicalUrl(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun setGoogleMapsCoordinates(lat: Float, lng: Float, name: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$lng ($name)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}