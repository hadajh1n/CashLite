package com.example.cashlite.ui.fragment.main

import android.app.AlertDialog
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.core.utils.format.formatMoney
import com.example.cashlite.data.dataclass.history.HistoryItem
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.databinding.DialogConfirmationDeleteTransactionBinding
import com.example.cashlite.databinding.DialogMonthYearPickerBinding
import com.example.cashlite.ui.adapter.HistoryAdapter
import com.example.cashlite.databinding.FragmentMainHistoryBinding
import com.example.cashlite.ui.viewModel.main.HistoryUiState
import com.example.cashlite.ui.viewModel.main.HistoryViewModel
import java.util.Calendar

class HistoryMainFragment : Fragment() {

    private var _binding: FragmentMainHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private val adapter = HistoryAdapter { transaction ->
        openDetails(transaction)
    }
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupClicks()
        observeViewModel()
        setupItemTouchHelper()

        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.rvHistory?.adapter = null
        itemTouchHelper?.attachToRecyclerView(null)
        itemTouchHelper = null
        _binding = null
    }

    private fun setupAdapter() = with(binding) {
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapter
    }

    private fun setupClicks() = with(binding) {
        btnHamburger.setOnClickListener { showMenu(it) }

        cvMonthYear.setOnClickListener { showMonthYearPicker() }
    }

    private fun openDetails(transaction: TransactionUI) {
        val action = HistoryMainFragmentDirections
            .actionHistoryToTransactionDetail(transaction)

        findNavController().navigate(action)
    }

    private fun showMonthYearPicker() {
        val dialogBinding = DialogMonthYearPickerBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()

        val months = resources.getStringArray(R.array.months_full)
        val currentFilter = viewModel.filter.value

        dialogBinding.monthPicker.apply {
            minValue = 0
            maxValue = 11
            displayedValues = months
            value = currentFilter?.month ?: Calendar.getInstance().get(Calendar.MONTH)
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        dialogBinding.yearPicker.apply {
            val curYear = Calendar.getInstance().get(Calendar.YEAR)
            minValue = curYear - 10
            maxValue = curYear + 2
            value = currentFilter?.year ?: curYear
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        val textColor = ContextCompat.getColor(requireContext(), R.color.white)
        val dividerColor = ContextCompat.getColor(requireContext(), R.color.white)

        setNumberPickerTextColor(dialogBinding.monthPicker, textColor)
        setNumberPickerTextColor(dialogBinding.yearPicker, textColor)

        setDividerColor(dialogBinding.monthPicker, dividerColor)
        setDividerColor(dialogBinding.yearPicker, dividerColor)

        dialogBinding.btnOk.setOnClickListener {
            viewModel.setFilter(dialogBinding.monthPicker.value, dialogBinding.yearPicker.value)
            dialog.dismiss()
        }

        dialogBinding.btnAllTime.setOnClickListener {
            viewModel.setAllTimeFilter()
            dialog.dismiss()
        }

        dialogBinding.btnReset.setOnClickListener {
            viewModel.resetToCurrentDate()

            val cal = Calendar.getInstance()
            dialogBinding.monthPicker.value = cal.get(Calendar.MONTH)
            dialogBinding.yearPicker.value = cal.get(Calendar.YEAR)

            dialog.dismiss()
        }

        dialogBinding.btnExit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int) {
        try {
            for (i in 0 until numberPicker.childCount) {
                val child = numberPicker.getChildAt(i)
                if (child is EditText) {
                    child.setTextColor(color)
                }
            }
            val field = NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
            field.isAccessible = true
            val paint = field.get(numberPicker) as Paint
            paint.color = color
            numberPicker.invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDividerColor(numberPicker: NumberPicker, color: Int) {
        try {
            val field = NumberPicker::class.java.getDeclaredField("mSelectionDivider")
            field.isAccessible = true
            field.set(numberPicker, ColorDrawable(color))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeViewModel() = with(binding) {
        viewModel.uiHistoryState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is HistoryUiState.Loading -> {
                    progressBarForHistory.visibility = View.VISIBLE
                    llEmptyList.visibility = View.GONE
                }

                is HistoryUiState.Empty -> {
                    progressBarForHistory.visibility = View.GONE
                    llEmptyList.visibility = View.VISIBLE
                    adapter.submitList(emptyList())
                }

                is HistoryUiState.Content -> {
                    progressBarForHistory.visibility = View.GONE
                    llEmptyList.visibility = View.GONE
                    adapter.submitList(state.items)
                }
            }
        }

        viewModel.filteredTotals.observe(viewLifecycleOwner) { state ->
            tvTotalExpense.text = if (state.totalExpense == 0.0) {
                "${state.totalExpense.formatMoney()} ₽"
            } else {
                "-${state.totalExpense.formatMoney()} ₽"
            }
            tvTotalIncome.text  = "${state.totalIncome.formatMoney()} ₽"
            tvTotalBalance.text = "${state.totalBalance.formatMoney()} ₽"
        }

        viewModel.monthYearLabel.observe(viewLifecycleOwner) { filter ->
            if (filter.isAllTime) {
                tvMonth.text = getString(R.string.all_time_label)
                tvYear.text = getString(R.string.transactions_label)
            } else {
                val months = resources.getStringArray(R.array.months_full)
                tvMonth.text = months[filter.month]
                tvYear.text = filter.year.toString()
            }
        }
    }

    private fun showMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor, Gravity.END)
        popup.menuInflater.inflate(R.menu.history_hamburger_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_delete_all -> {
                    showConfirmationDeleteAllTransactionsDialog()
                    true
                }
                R.id.menu_delete_import -> {
                    showConfirmationDeleteImportedTransactionsDialog()
                    true
                }
                else -> false
            }
        }

        popup.setForceShowIcon(true)
        popup.show()
    }

    private fun showConfirmationDeleteAllTransactionsDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.dialogDeleteAllTransactions)
            .setPositiveButton(R.string.dialogButtonYes) { _, _ ->
                viewModel.onDeleteAllTransactions()
            }
            .setNegativeButton(R.string.dialogButtonNo, null)
            .show()
    }

    private fun showConfirmationDeleteImportedTransactionsDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.dialogDeleteImportedTransactions)
            .setPositiveButton(R.string.dialogButtonYes) { _, _ ->
                viewModel.onDeleteImportedTransactions()
            }
            .setNegativeButton(R.string.dialogButtonNo, null)
            .show()
    }

    private fun setupItemTouchHelper() = with(binding) {

        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewHolder.adapterPosition
                val item = adapter.getItem(position)

                if (item is HistoryItem.TransactionItem) {
                    showDeleteConfirmationDialog(item.transaction, position)
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is HistoryAdapter.DateViewHolder) return 0

                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        }

        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper?.attachToRecyclerView(binding.rvHistory)
    }

    private fun showDeleteConfirmationDialog(transaction: TransactionUI, position: Int) {
        val dialogBinding = DialogConfirmationDeleteTransactionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(
            requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnConfirmationDeleteTransaction.setOnClickListener {
            viewModel.onSwipeRemoveTransaction(transaction)
            dialog.dismiss()
        }

        dialogBinding.btnCancelDeleteTransaction.setOnClickListener {
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        dialog.show()
    }
}