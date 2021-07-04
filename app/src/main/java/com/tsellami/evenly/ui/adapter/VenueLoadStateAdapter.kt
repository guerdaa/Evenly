package com.tsellami.evenly.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsellami.evenly.databinding.NetworkStateItemBinding

class VenueLoadStateAdapter(
    private val retryCallback: () -> Unit
) : LoadStateAdapter<VenueLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        val binding =
            NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NetworkStateItemViewHolder(binding)
    }

    inner class NetworkStateItemViewHolder(
        private val binding: NetworkStateItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retryCallback()
            }
        }

        fun bindTo(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}

