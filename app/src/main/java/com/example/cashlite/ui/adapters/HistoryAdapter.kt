package com.example.cashlite.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    val historyList = mutableListOf<Transaction>()

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHistoryBinding.bind(view)

        fun bind(transaction: Transaction) = with(binding) {
            tvDate.text = transaction.date
            tvAmount.text = String.format("%.2f", transaction.amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}