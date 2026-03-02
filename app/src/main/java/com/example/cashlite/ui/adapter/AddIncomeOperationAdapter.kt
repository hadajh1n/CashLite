package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.databinding.ItemAddNewIncomeOperationBinding

class AddIncomeOperationAdapter(
    private val onCategoryClick: (Transaction.Income) -> Unit,
) : RecyclerView.Adapter<AddIncomeOperationAdapter.AddIncomeViewHolder>() {

    private val incomeCategoriesList = mutableListOf<Transaction.Income>()
    private var selectedPosition = RecyclerView.NO_POSITION

    fun submitList(newList: List<Transaction.Income>) {
        incomeCategoriesList.clear()
        incomeCategoriesList.addAll(newList)
        notifyDataSetChanged()
    }

    private fun updateSelectedPosition(newPosition: Int) {
        if (newPosition == selectedPosition) return

        val previousPosition = selectedPosition
        selectedPosition = newPosition

        if (previousPosition != RecyclerView.NO_POSITION) notifyItemChanged(previousPosition)

        notifyItemChanged(selectedPosition)
    }

    inner class AddIncomeViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemAddNewIncomeOperationBinding.bind(item)

        fun bind(item: Transaction.Income, position: Int) = with(binding) {
            imIcon.setImageResource(item.imageId)
            imIcon.isSelected = position == selectedPosition
            tvIncomeTitle.text = item.categoryName

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
    ): AddIncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_add_new_income_operation, parent, false
        )
        return AddIncomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddIncomeViewHolder, position: Int) {
        holder.bind(incomeCategoriesList[position], position)
    }

    override fun getItemCount(): Int = incomeCategoriesList.size
}