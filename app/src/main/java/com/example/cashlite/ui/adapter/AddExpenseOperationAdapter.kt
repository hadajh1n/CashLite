package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.IconExpenseCategory
import com.example.cashlite.databinding.ItemAddNewExpenseOperationBinding

class AddExpenseOperationAdapter(
    private val onCategoryClick: (IconExpenseCategory) -> Unit,
) : RecyclerView.Adapter<AddExpenseOperationAdapter.AddExpenceViewHolder>() {

    private val expenseCategoriesList = mutableListOf<IconExpenseCategory>()
    private var selectedPosition = RecyclerView.NO_POSITION

    fun submitList(newList: List<IconExpenseCategory>) {
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

    inner class AddExpenceViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemAddNewExpenseOperationBinding.bind(item)

        fun bind(item: IconExpenseCategory, position: Int) = with(binding) {
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
    ): AddExpenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_add_new_expense_operation, parent, false
        )
        return AddExpenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddExpenceViewHolder, position: Int) {
        holder.bind(expenseCategoriesList[position], position)
    }

    override fun getItemCount(): Int = expenseCategoriesList.size
}