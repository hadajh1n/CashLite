package com.example.cashlite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.HistoryItem
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.databinding.ItemDateHeaderHistoryBinding
import com.example.cashlite.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_TRANSACTION = 1
    }

    private var items = mutableListOf<HistoryItem>()

    fun submitList(newItems: List<HistoryItem>) {

        val diffCallback = HistoryDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this@HistoryAdapter)
    }

    fun getItem(position: Int): HistoryItem {
        return items[position]
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDateHeaderHistoryBinding.bind(view)

        fun bind(date: String) = with(binding) {
            tvDate.text = date
        }
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

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HistoryItem.DateHeader -> TYPE_HEADER
            is HistoryItem.TransactionItem -> TYPE_TRANSACTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_date_header_history, parent, false)
                DateViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_history, parent, false)
                HistoryViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is HistoryItem.DateHeader -> {
                (holder as DateViewHolder).bind(item.date)
            }
            is HistoryItem.TransactionItem -> {
                (holder as HistoryViewHolder).bind(item.transaction)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}

class HistoryDiffCallback(
    private val oldList: List<HistoryItem>,
    private val newList: List<HistoryItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is HistoryItem.DateHeader && newItem is HistoryItem.DateHeader ->
                oldItem.date == newItem.date

            oldItem is HistoryItem.TransactionItem && newItem is HistoryItem.TransactionItem ->
                oldItem.transaction == newItem.transaction

            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}