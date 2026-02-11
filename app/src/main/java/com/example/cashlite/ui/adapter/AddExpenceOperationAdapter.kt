package com.example.cashlite.ui.adapter

import com.example.cashlite.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.data.dataclass.ExpenceIcons
import com.example.cashlite.databinding.ItemAddNewExpenceOperationBinding

class AddExpenceOperationAdapter(
    private val onClick: (ExpenceIcons) -> Unit
) : RecyclerView.Adapter<AddExpenceOperationAdapter.AddExpenceViewHolder>() {

    private val expenceList = ArrayList<ExpenceIcons>()

    inner class AddExpenceViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val binding = ItemAddNewExpenceOperationBinding.bind(item)

        fun bind(expence: ExpenceIcons) = with(binding) {
            imIcon.setImageResource(expence.imageId)
            tvExpenceTitle.text = expence.title
            root.setOnClickListener {
                onClick(expence)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddExpenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_add_new_expence_operation, parent, false
        )
        return AddExpenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddExpenceViewHolder, position: Int) {
        holder.bind(expenceList[position])
    }

    override fun getItemCount(): Int = expenceList.size

    fun submitList(list: List<ExpenceIcons>) {
        expenceList.clear()
        expenceList.addAll(list)
        notifyDataSetChanged()
    }
}