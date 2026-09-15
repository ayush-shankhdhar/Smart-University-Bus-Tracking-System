package com.example.bustracking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bustracking.data.local.entity.ComplaintEntity
import com.example.bustracking.databinding.ItemComplaintCardBinding

class ComplaintListAdapter :
    ListAdapter<ComplaintEntity, ComplaintListAdapter.ComplaintViewHolder>(ComplaintDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding =
            ItemComplaintCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ComplaintViewHolder(private val binding: ItemComplaintCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(complaint: ComplaintEntity) {
            binding.tvComplaintAuthor.text = "Author: ${complaint.author}"
            binding.tvComplaintStatus.text = complaint.status
            binding.tvComplaintBody.text = complaint.complain
        }
    }

    class ComplaintDiffCallback : DiffUtil.ItemCallback<ComplaintEntity>() {
        override fun areItemsTheSame(oldItem: ComplaintEntity, newItem: ComplaintEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ComplaintEntity, newItem: ComplaintEntity): Boolean {
            return oldItem == newItem
        }
    }
}
