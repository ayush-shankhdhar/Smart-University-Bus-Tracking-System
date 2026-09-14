package com.example.bustracking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bustracking.R
import com.example.bustracking.data.local.entity.BusEntity
import com.example.bustracking.databinding.ItemBusCardBinding

class BusListAdapter(
    private val onBusClick: (BusEntity) -> Unit,
    private val onFavToggle: (BusEntity) -> Unit
) : ListAdapter<BusEntity, BusListAdapter.BusViewHolder>(BusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        val binding = ItemBusCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BusViewHolder(private val binding: ItemBusCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bus: BusEntity) {
            binding.tvBusName.text = bus.busName.ifEmpty { "Campus Bus" }
            binding.tvRouteName.text = bus.routeName
            binding.tvStatusBadge.text = bus.status
            binding.tvEtaBadge.text = "ETA: ${bus.etaMinutes} mins"

            binding.root.setOnClickListener {
                onBusClick(bus)
            }

            binding.btnFavToggle.setOnClickListener {
                onFavToggle(bus)
            }
        }
    }

    class BusDiffCallback : DiffUtil.ItemCallback<BusEntity>() {
        override fun areItemsTheSame(oldItem: BusEntity, newItem: BusEntity): Boolean {
            return oldItem.busName == newItem.busName
        }

        override fun areContentsTheSame(oldItem: BusEntity, newItem: BusEntity): Boolean {
            return oldItem == newItem
        }
    }
}
