package com.example.cashlite.ui.fragment.main

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.graphs.Period
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

    private var lastPeriod: Period? = null
    private var lastType: CategoryType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

            val state = viewModel.graphState.value
            val currentRange = state?.range

            list.forEach { item ->
                val btn = MaterialButton(
                    requireContext(),
                    null,
                    com.google.android.material.R.attr.materialButtonOutlinedStyle
                ).apply {
                    text = item.label

                    setTextColor(ContextCompat.getColorStateList(
                        context, R.color.btn_select_graphs_sort_date_text_nav_selector)
                    )
                    backgroundTintList = ContextCompat.getColorStateList(
                        context, R.color.btn_select_graphs_sort_date_nav_selector
                    )
                    strokeColor = ContextCompat.getColorStateList(
                        context, R.color.btn_select_graphs_sort_date_nav_selector
                    )

                    isSelected = item.key == currentRange

                    insetTop = 0
                    insetBottom = 0
                    cornerRadius = 20

                    setOnClickListener {
                        viewModel.setRange(item.key)
                    }
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(12, 0, 12, 0)
                }
                llGraphsExpenseWeekSort.addView(btn, params)
            }

            val needsScroll = state?.period != lastPeriod || state?.type != lastType

            if (needsScroll) {
                lastPeriod = state?.period
                lastType = state?.type

                horizontalScrollView.post {
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT)
                }
            }
        }
    }

    private fun observeGraph() = with(binding) {
        viewModel.graphData.observe(viewLifecycleOwner) { data ->
            if (data.pieEntries.isEmpty()) {
                pieChart.clear()
                pieChart.setNoDataText("Нет операций за этот период")
                pieChart.setNoDataTextColor(android.graphics.Color.WHITE)
                adapter.submitList(emptyList())
                return@observe
            }

            val resolvedColors = data.categoryColors.map {
                try { ContextCompat.getColor(requireContext(), it) }
                catch (e: Exception) { it }
            }

            val dataSet = PieDataSet(data.pieEntries, "").apply {
                colors = resolvedColors
                sliceSpace = 3f
                setDrawValues(false)
            }

            pieChart.apply {
                this.data = PieData(dataSet)

                minOffset = 0f
                setExtraOffsets(10f, 5f, 85f, 5f)

                holeRadius = 52f
                transparentCircleRadius = 0f
                setHoleColor(android.graphics.Color.TRANSPARENT)
                setDrawEntryLabels(false)
                description.isEnabled = false
                isRotationEnabled = false

                legend.apply {
                    isEnabled = true
                    textSize = 14f
                    formSize = 14f
                    yEntrySpace = 10f
                    textColor = android.graphics.Color.WHITE
                    orientation = Legend.LegendOrientation.VERTICAL
                    verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    isWordWrapEnabled = true
                    maxSizePercent = 0.45f
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