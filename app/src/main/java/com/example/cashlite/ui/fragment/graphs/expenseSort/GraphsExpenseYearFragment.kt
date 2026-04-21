package com.example.cashlite.ui.fragment.graphs.expenseSort

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.cashlite.databinding.FragmentGraphsExpenseYearBinding
import com.example.cashlite.ui.viewModel.graphs.expenseSort.GraphsExpenseYearViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.getValue

class GraphsExpenseYearFragment : Fragment() {

    private var _binding: FragmentGraphsExpenseYearBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GraphsExpenseYearViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphsExpenseYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPieChart()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupPieChart() = with(binding.pieChart) {
        description.isEnabled = false
        isDrawHoleEnabled = true
        holeRadius = 70f
        transparentCircleRadius = 0f

        setUsePercentValues(false)
        setDrawEntryLabels(false)

        extraRightOffset = 0f

        animateY(500)
    }

    private fun observeViewModel() {
        viewModel.pieEntries.observe(viewLifecycleOwner) { entries ->
            val pieChart = binding.pieChart
            pieChart.setExtraOffsets(5f, 5f, 5f, 5f)

            if (entries.isEmpty()) {
                pieChart.setNoDataText("Транзакций нет")
                pieChart.setNoDataTextColor(Color.WHITE)
                pieChart.invalidate()
                return@observe
            }

            val dataSet = PieDataSet(entries, null)
            dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
            dataSet.setDrawValues(false)
            dataSet.sliceSpace = 3f

            val pieData = PieData(dataSet)
            pieData.setValueFormatter(PercentFormatter(pieChart))

            pieChart.data = pieData

            val total = entries.sumOf { it.value.toDouble() }.toFloat()

            val legendEntries = entries.mapIndexed { index, entry ->

                val percent = (entry.value / total) * 100
                val labelWithPercent = "${entry.label} ${"%.1f".format(percent)}%"

                LegendEntry(
                    labelWithPercent,
                    Legend.LegendForm.CIRCLE,
                    14f,
                    14f,
                    null,
                    dataSet.colors[index % dataSet.colors.size]
                )
            }

            pieChart.legend.apply {
                isEnabled = true
                setCustom(legendEntries)

                orientation = Legend.LegendOrientation.VERTICAL
                verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

                textSize = 16f
                textColor = Color.WHITE
                formSize = 14f
                xEntrySpace = 0f
                yEntrySpace = 0f
                xOffset = 0f
                yOffset = 0f
            }

            pieChart.invalidate()
        }
    }
}