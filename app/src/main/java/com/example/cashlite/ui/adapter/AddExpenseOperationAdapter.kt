package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.databinding.ItemAddNewExpenseOperationBinding

class AddExpenseOperationAdapter(
    private val onCategoryClick: (Transaction.Expense) -> Unit,
) : RecyclerView.Adapter<AddExpenseOperationAdapter.AddExpenseViewHolder>() {

    private val expenseCategoriesList = mutableListOf<Transaction.Expense>()
    private var selectedPosition = RecyclerView.NO_POSITION

    fun submitList(newList: List<Transaction.Expense>) {

        val diffCallback = ExpenseDiffCallback(expenseCategoriesList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        expenseCategoriesList.clear()
        expenseCategoriesList.addAll(newList)

        diffResult.dispatchUpdatesTo(this@AddExpenseOperationAdapter)
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
            tvExpenseTitle.text = itemView.context.getString(item.categoryNameRes)

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

class ExpenseDiffCallback(
    private val oldList: List<Transaction.Expense>,
    private val newList: List<Transaction.Expense>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.categoryNameRes == newItem.categoryNameRes                 // КОГДА ДОБАВИШЬ БД, ЗАМЕНИ НА ID ВМЕСТО ПРОВЕРКИ ПО КАТЕГОРИЯМ
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}