package com.example.cashlite.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.core.utils.format.formatMoney
import com.example.cashlite.data.dataclass.graphs.CategoryChartItem
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.room.category.CategoryType
import com.example.cashlite.databinding.ItemCategoryChartBinding

class CategoryChartAdapter() : RecyclerView.Adapter<CategoryChartAdapter.CategoryChartViewHolder>() {

    private var items: List<CategoryChartItem> = emptyList()

    fun submitList(newList: List<CategoryChartItem>) {
        val diffCallback = CategoryDetailsChartDiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChartViewHolder {
        val binding = ItemCategoryChartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryChartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CategoryChartViewHolder(
        private val binding: ItemCategoryChartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryChartItem) = with(binding) {

            imCategoryIcon.setImageResource(item.imageId)
            imCategoryIcon.setColorFilter(itemView.context.getColor(item.color))

            tvCategoryChart.text = itemView.context.getString(
                CategoryKeys.getCategoryNameRes(item.categoryName)
            )

            val isExpense = item.type == CategoryType.EXPENSE
            val sign = if (isExpense) "-" else "+"

            tvAmountChart.text = "$sign${item.amount.formatMoney()} ₽"
            tvPercentChart.text = "%.1f%%".format(item.percent)
        }
    }
}

class CategoryDetailsChartDiffCallback(
    private val oldList: List<CategoryChartItem>,
    private val newList: List<CategoryChartItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].categoryName == newList[newItemPosition].categoryName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}