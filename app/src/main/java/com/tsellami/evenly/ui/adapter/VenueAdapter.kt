package com.tsellami.evenly.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tsellami.evenly.databinding.VenueItemLayoutBinding
import com.tsellami.evenly.rooms.Venue

class VenueAdapter(private val listener: OnItemListener):
    PagingDataAdapter<Venue, VenueAdapter.VenueViewHolder>(DiffComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = VenueItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val currentVenue = getItem(position)
        holder.bind(currentVenue)
    }

    inner class VenueViewHolder(private val binding: VenueItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onClick(getItem(position)!!)
                }
            }
        }

        fun bind(venue: Venue?) {
            venue?.let {
                binding.name.text = venue.name
                binding.category.text = venue.categoryName
                binding.distance.text = "${venue.distance}m"
                binding.address.text = venue.address
            }

        }
    }

    interface OnItemListener {
        fun onClick(venue: Venue)
    }

    class DiffComparator: DiffUtil.ItemCallback<Venue>() {
        override fun areItemsTheSame(
            oldItem: Venue,
            newItem: Venue
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Venue,
            newItem: Venue
        ): Boolean =
            oldItem == newItem

    }
}