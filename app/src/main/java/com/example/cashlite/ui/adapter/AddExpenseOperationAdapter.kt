package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.history.CategoryUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.databinding.ItemAddNewExpenseOperationBinding

class AddExpenseOperationAdapter(
    private val onCategoryClick: (CategoryUI) -> Unit,
) : RecyclerView.Adapter<AddExpenseOperationAdapter.AddExpenseViewHolder>() {

    private val expenseCategoriesList = mutableListOf<CategoryUI>()
    private var selectedCategoryId: Int? = null

    fun submitList(newList: List<CategoryUI>) {

        val diffCallback = ExpenseDiffCallback(expenseCategoriesList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        expenseCategoriesList.clear()
        expenseCategoriesList.addAll(newList)

        diffResult.dispatchUpdatesTo(this@AddExpenseOperationAdapter)
    }

    inner class AddExpenseViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemAddNewExpenseOperationBinding.bind(item)

        fun bind(item: CategoryUI) = with(binding) {
            imIcon.setImageResource(item.imageId)
            imIcon.isSelected = item.idCategory == selectedCategoryId
            tvExpenseTitle.text = itemView.context.getString(
                CategoryKeys.getCategoryNameRes(item.categoryName)
            )
            val textSelector = ContextCompat.getColorStateList(
                itemView.context, R.color.text_category_selector
            )
            tvExpenseTitle.setTextColor(textSelector)

            root.setOnClickListener {
                val oldPosition = expenseCategoriesList
                    .indexOfFirst { it.idCategory == selectedCategoryId }

                selectedCategoryId = item.idCategory

                val newPosition = adapterPosition

                if (oldPosition != -1) notifyItemChanged(oldPosition)
                notifyItemChanged(newPosition)

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
        holder.bind(expenseCategoriesList[position])
    }

    override fun getItemCount(): Int = expenseCategoriesList.size
}

class ExpenseDiffCallback(
    private val oldList: List<CategoryUI>,
    private val newList: List<CategoryUI>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idCategory == newList[newItemPosition].idCategory
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}