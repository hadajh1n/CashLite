package com.example.cashlite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.NewExpenseTransaction
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items = mutableListOf<Transaction>()

    fun submitList(newItems: List<Transaction>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHistoryBinding.bind(view)

        fun bind(item: Transaction) = with(binding) {

            when (item) {
                is Transaction.Expense -> {
                    imCategory.setImageResource(item.imageId)
                    tvCategory.text = item.categoryName
                    tvNote.text = item.note
                    tvAmount.text = "-${item.amount} ₽"
                }

                is Transaction.Income -> {
                    imCategory.setImageResource(item.imageId)
                    tvCategory.text = item.categoryName
                    tvNote.text = item.note
                    tvAmount.text = "+${item.amount} ₽"
                }
            }
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