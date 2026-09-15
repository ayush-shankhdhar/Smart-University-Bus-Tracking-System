package com.example.bustracking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bustracking.data.local.entity.ScheduleEntity
import com.example.bustracking.databinding.ItemScheduleCardBinding

class ScheduleListAdapter :
    ListAdapter<ScheduleEntity, ScheduleListAdapter.ScheduleViewHolder>(ScheduleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding =
            ItemScheduleCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ScheduleViewHolder(private val binding: ItemScheduleCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: ScheduleEntity) {
            binding.tvScheduleTitle.text = "${schedule.routeName} (${schedule.busName})"
            binding.tvFrequency.text = schedule.frequency
            binding.tvScheduleDetails.text =
                "Departure: ${schedule.departureTime} | Arrival: ${schedule.arrivalTime}"
            binding.tvRawScheduleText.text = schedule.rawText
        }
    }

    class ScheduleDiffCallback : DiffUtil.ItemCallback<ScheduleEntity>() {
        override fun areItemsTheSame(oldItem: ScheduleEntity, newItem: ScheduleEntity): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(oldItem: ScheduleEntity, newItem: ScheduleEntity): Boolean {
            return oldItem == newItem
        }
    }
}
