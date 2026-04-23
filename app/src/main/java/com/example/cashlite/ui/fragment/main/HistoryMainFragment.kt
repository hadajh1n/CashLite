package com.example.cashlite.ui.fragment.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.core.utils.format.formatMoney
import com.example.cashlite.data.dataclass.history.HistoryItem
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.ui.adapter.HistoryAdapter
import com.example.cashlite.databinding.FragmentMainHistoryBinding
import com.example.cashlite.ui.viewModel.main.HistoryUiState
import com.example.cashlite.ui.viewModel.main.HistoryViewModel

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
        observeViewModel()
        onButtonHamburger()
        setupItemTouchHelper()
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

    private fun openDetails(transaction: TransactionUI) {
        val action = HistoryMainFragmentDirections
            .actionHistoryToTransactionDetail(transaction)

        findNavController().navigate(action)
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

        viewModel.totalTransaction.observe(viewLifecycleOwner) { state ->
            tvTotalExpense.text = if (state.totalExpense == 0.0) {
                "${state.totalExpense.formatMoney()} ₽"
            } else {
                "-${state.totalExpense.formatMoney()} ₽"
            }
            tvTotalIncome.text  = "${state.totalIncome.formatMoney()} ₽"
            tvTotalBalance.text = "${state.totalBalance.formatMoney()} ₽"
        }
    }

    private fun onButtonHamburger() = with(binding) {
        btnHamburger.setOnClickListener { showMenu(it) }
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
                    viewModel.onSwipeRemoveTransaction(item.transaction)
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
}