package com.example.jejakrempah.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jejakrempah.data.JejakRempahItem
import com.example.jejakrempah.databinding.ItemEventAvailableBinding

class HomeAdapter(
    private val onItemClick: (JejakRempahItem) -> Unit // Menambahkan parameter listener klik
) : ListAdapter<JejakRempahItem, HomeAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventAvailableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onItemClick) // Pasang listener klik ke ViewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class EventViewHolder(
        private val binding: ItemEventAvailableBinding,
        private val onItemClick: (JejakRempahItem) -> Unit // Menyimpan listener klik
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: JejakRempahItem) {
            binding.summary.text = event.rempah
            Glide.with(binding.image.context)
                .load(event.imageURLs.get(0))
                .into(binding.image)

            // Menangani klik item
            binding.root.setOnClickListener {
                onItemClick(event) // Panggil listener klik dengan item yang diklik
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JejakRempahItem>() {
            override fun areItemsTheSame(oldItem: JejakRempahItem, newItem: JejakRempahItem): Boolean {
                return oldItem.no == newItem.no
            }

            override fun areContentsTheSame(oldItem: JejakRempahItem, newItem: JejakRempahItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
