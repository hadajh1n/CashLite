package com.example.cashlite.ui.fragment.main

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.Period
import com.example.cashlite.data.room.category.CategoryType
import com.example.cashlite.databinding.FragmentMainGraphsBinding
import com.example.cashlite.ui.adapter.CategoryChartAdapter
import com.example.cashlite.ui.viewModel.main.GraphsViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.google.android.material.button.MaterialButton

class GraphsMainFragment : Fragment() {

    private var _binding: FragmentMainGraphsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GraphsViewModel by viewModels()
    private val adapter = CategoryChartAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainGraphsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupChartBase()
        setupClicks()
        observeState()
        observePeriods()
        observeGraph()
    }

    private fun setupAdapter() = with(binding) {
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = adapter
    }

    private fun setupClicks() = with(binding) {
        btnExpense.setOnClickListener { viewModel.setType(CategoryType.EXPENSE) }
        btnIncome.setOnClickListener { viewModel.setType(CategoryType.INCOME) }
        btnWeek.setOnClickListener { viewModel.setPeriod(Period.WEEK) }
        btnMonth.setOnClickListener { viewModel.setPeriod(Period.MONTH) }
        btnYear.setOnClickListener { viewModel.setPeriod(Period.YEAR) }
    }

    private fun observeState() = with(binding) {
        viewModel.graphState.observe(viewLifecycleOwner) {
            btnExpense.isSelected = it.type == CategoryType.EXPENSE
            btnIncome.isSelected = it.type == CategoryType.INCOME
            btnWeek.isSelected = it.period == Period.WEEK
            btnMonth.isSelected = it.period == Period.MONTH
            btnYear.isSelected = it.period == Period.YEAR
        }
    }

    private fun observePeriods() = with(binding) {
        viewModel.periods.observe(viewLifecycleOwner) { list ->
            llGraphsExpenseWeekSort.removeAllViews()
            val currentRange = viewModel.graphState.value?.range

            list.forEach { item ->
                val btn = MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                    text = item.label
                    val isSelected = (item.key == currentRange)

                    if (isSelected) {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.btnSelectGraphsNavSelector))
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        strokeWidth = 0
                    } else {
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        setStrokeColorResource(android.R.color.white)
                        strokeWidth = 1
                    }
                    insetTop = 0; insetBottom = 0
                    setOnClickListener { viewModel.setRange(item.key) }
                }

                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(12, 0, 12, 0)
                }
                llGraphsExpenseWeekSort.addView(btn, params)
            }

            // ПРОКРУТКА ВПРАВО: Текущий период теперь в конце списка
            horizontalScrollView.post {
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT)
            }
        }
    }

    private fun observeGraph() {
        viewModel.graphData.observe(viewLifecycleOwner) { data ->
            if (data.pieEntries.isEmpty()) {
                binding.pieChart.clear()
                binding.pieChart.setNoDataText("Нет операций за этот период")
                binding.pieChart.setNoDataTextColor(android.graphics.Color.WHITE)
                adapter.submitList(emptyList())
                return@observe
            }

            val resolvedColors = data.categoryColors.map {
                try { ContextCompat.getColor(requireContext(), it) } catch (e: Exception) { it }
            }

            val dataSet = PieDataSet(data.pieEntries, "").apply {
                colors = resolvedColors
                sliceSpace = 3f
                setDrawValues(false)
            }

            binding.pieChart.apply {
                this.data = PieData(dataSet)
                setExtraOffsets(15f, 20f, 65f, 20f) // Сжимаем график для легенды справа
                holeRadius = 55f
                transparentCircleRadius = 0f
                setHoleColor(android.graphics.Color.TRANSPARENT)
                setDrawEntryLabels(false)
                description.isEnabled = false

                legend.apply {
                    isEnabled = true
                    textSize = 12f
                    yEntrySpace = 7f
                    textColor = android.graphics.Color.WHITE
                    orientation = Legend.LegendOrientation.VERTICAL
                    verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                }
                animateY(700)
                invalidate()
            }
            adapter.submitList(data.categoryDetails)
        }
    }

    private fun setupChartBase() = with(binding.pieChart) {
        setNoDataText("Загрузка...")
        setNoDataTextColor(android.graphics.Color.WHITE)
        description.isEnabled = false
        isRotationEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}