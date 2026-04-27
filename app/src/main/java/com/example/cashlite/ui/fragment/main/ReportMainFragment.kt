package com.example.cashlite.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cashlite.core.utils.format.formatMoney
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.databinding.FragmentMainReportBinding
import com.example.cashlite.ui.viewModel.main.ReportViewModel

class ReportMainFragment : Fragment() {

    private var _binding: FragmentMainReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() = with(binding) {
        viewModel.reportData.observe(viewLifecycleOwner) { data ->
            tvSavingsValue.text = "${data.savingsRate.toInt()}%"
            tvReportIncome.text = "${data.totalIncome.formatMoney()} ₽"
            tvReportExpense.text = "${data.totalExpense.formatMoney()} ₽"

            tvCountValue.text = "Операций за месяц: ${data.transactionCount}"
            tvAvgValue.text = "Средний чек: ${data.averageTransaction.formatMoney()} ₽"

            tvMaxValue.text = data.maxExpense?.let { (name, amount) ->
                "Рекордный расход: ${getString(CategoryKeys.getCategoryNameRes(name))} " +
                        "(${amount.formatMoney()} ₽)"
            } ?: "Рекордных расходов пока нет"

            tvTopCategoriesReport.text = if (data.topCategories.isNotEmpty()) {
                data.topCategories.mapIndexed { index, pair ->
                    "${index + 1}. ${getString(CategoryKeys.getCategoryNameRes(pair.first))} — " +
                            "${pair.second.formatMoney()} ₽"
                }.joinToString("\n")
            } else {
                "Данных для топа пока недостаточно"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}