package com.example.cashlite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.NewExpenseTransaction
import com.example.cashlite.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items = mutableListOf<NewExpenseTransaction>()

    fun submitList(newItems: List<NewExpenseTransaction>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHistoryBinding.bind(view)

        fun bind(item: NewExpenseTransaction) = with(binding) {
            imCategory.setImageResource(item.category.imageId)
            tvCategory.text = item.category.categoryName
            tvNote.text = if (item.note.isNotEmpty()) item.note else ""
            tvAmount.text = "-${item.amount} ₽"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}