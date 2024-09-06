package com.example.quote.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quote.data.entity.QuoteRoomEntity
import com.example.quote.databinding.ItemSavedQuotesBinding

class SavedQuotesAdapter(
    private val cp: (String, View) -> Unit,
    private val oP: (QuoteRoomEntity) -> Unit
) :
    RecyclerView.Adapter<SavedQuotesAdapter.ViewHolder>() {

    private val callBack = object : DiffUtil.ItemCallback<QuoteRoomEntity>() {
        override fun areItemsTheSame(oldItem: QuoteRoomEntity, newItem: QuoteRoomEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: QuoteRoomEntity,
            newItem: QuoteRoomEntity
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val diffUtil = AsyncListDiffer(this, callBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemSavedQuotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(diffUtil.currentList[position])
    }

    inner class ViewHolder(private val binding: ItemSavedQuotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuoteRoomEntity) {
            binding.quote.text = item.content
            binding.txtCategory.text = item.category
            binding.author.text = item.author
            itemView.setOnClickListener {
                oP(item)
            }
            itemView.setOnLongClickListener {
                cp(item.content, binding.quoteContainer)
                true
            }
        }
    }

    fun submitList(list: List<QuoteRoomEntity>) {
        diffUtil.submitList(list)
    }
}