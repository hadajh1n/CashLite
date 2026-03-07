package com.example.cashlite.ui.fragment

import com.example.cashlite.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashlite.ui.adapter.HistoryAdapter
import com.example.cashlite.databinding.FragmentHistoryBinding
import com.example.cashlite.ui.viewModel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        observeViewModel()
        onAddNewOperationButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter() = with(binding) {
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapter
    }

    private fun observeViewModel() = with(binding) {
        viewModel.historyItems.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)

            if (list.isEmpty()) llEmptyList.visibility = View.VISIBLE else
                llEmptyList.visibility = View.GONE
        }

        viewModel.totalTransaction.observe(viewLifecycleOwner) { state ->
            tvTotalExpense.text = "${state.totalExpense}"
            tvTotalIncome.text  = "${state.totalIncome}"
            tvTotalBalance.text = "${state.totalBalance}"
        }
    }

    private fun onAddNewOperationButton() {
        binding.btnAddOperation.setOnClickListener {
            findNavController()
                .navigate(R.id.addNewOperationActivity)
        }
    }
}
