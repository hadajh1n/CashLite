package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.CategoryUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.databinding.ItemAddNewIncomeOperationBinding

class AddIncomeOperationAdapter(
    private val onCategoryClick: (CategoryUI) -> Unit,
) : RecyclerView.Adapter<AddIncomeOperationAdapter.AddIncomeViewHolder>() {

    private val incomeCategoriesList = mutableListOf<CategoryUI>()
    private var selectedCategoryId: Int? = null

    fun submitList(newList: List<CategoryUI>) {

        val diffCallback = IncomeDiffCallback(incomeCategoriesList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        incomeCategoriesList.clear()
        incomeCategoriesList.addAll(newList)

        diffResult.dispatchUpdatesTo(this@AddIncomeOperationAdapter)
    }

    inner class AddIncomeViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemAddNewIncomeOperationBinding.bind(item)

        fun bind(item: CategoryUI) = with(binding) {
            imIcon.setImageResource(item.imageId)
            imIcon.isSelected = item.idCategory == selectedCategoryId
            tvIncomeTitle.text = itemView.context.getString(
                CategoryKeys.getCategoryNameRes(item.categoryName)
            )

            root.setOnClickListener {
                val oldPosition = incomeCategoriesList
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
    ): AddIncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_add_new_income_operation, parent, false
        )
        return AddIncomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddIncomeViewHolder, position: Int) {
        holder.bind(incomeCategoriesList[position])
    }

    override fun getItemCount(): Int = incomeCategoriesList.size
}

class IncomeDiffCallback(
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