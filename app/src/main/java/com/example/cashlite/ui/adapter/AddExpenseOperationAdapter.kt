package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.databinding.ItemAddNewExpenseOperationBinding

class AddExpenseOperationAdapter(
    private val onCategoryClick: (Transaction.Expense) -> Unit,
) : RecyclerView.Adapter<AddExpenseOperationAdapter.AddExpenseViewHolder>() {

    private val expenseCategoriesList = mutableListOf<Transaction.Expense>()
    private var selectedPosition = RecyclerView.NO_POSITION

    fun submitList(newList: List<Transaction.Expense>) {
        expenseCategoriesList.clear()
        expenseCategoriesList.addAll(newList)
        notifyDataSetChanged()
    }

    private fun updateSelectedPosition(newPosition: Int) {
        if (newPosition == selectedPosition) return

        val previousPosition = selectedPosition
        selectedPosition = newPosition

        if (previousPosition != RecyclerView.NO_POSITION) notifyItemChanged(previousPosition)

        notifyItemChanged(selectedPosition)
    }

    inner class AddExpenseViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemAddNewExpenseOperationBinding.bind(item)

        fun bind(item: Transaction.Expense, position: Int) = with(binding) {
            imIcon.setImageResource(item.imageId)
            imIcon.isSelected = position == selectedPosition
            tvExpenseTitle.text = item.categoryName

            root.setOnClickListener {
                val currentPosition = bindingAdapterPosition
                updateSelectedPosition(currentPosition)
                onCategoryClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_add_new_expense_operation, parent, false
        )
        return AddExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddExpenseViewHolder, position: Int) {
        holder.bind(expenseCategoriesList[position], position)
    }

    override fun getItemCount(): Int = expenseCategoriesList.size
}