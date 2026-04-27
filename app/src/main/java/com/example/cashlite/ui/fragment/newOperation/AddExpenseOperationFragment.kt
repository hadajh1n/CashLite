package com.example.cashlite.ui.fragment.newOperation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cashlite.R
import com.example.cashlite.core.utils.Constants
import com.example.cashlite.core.utils.format.DecimalDigitsInputFilter
import com.example.cashlite.core.utils.format.formatDate
import com.example.cashlite.core.utils.format.setupAmountLogic
import com.example.cashlite.data.dataclass.history.CategoryUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.databinding.FragmentAddExpenseOperationBinding
import com.example.cashlite.databinding.ViewAddCategoriesPanelBinding
import com.example.cashlite.ui.adapter.AddExpenseOperationAdapter
import com.example.cashlite.ui.viewModel.newOperation.AddExpenseViewModel
import java.util.Calendar

class AddExpenseOperationFragment : Fragment() {

    private var _binding: FragmentAddExpenseOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddExpenseViewModel by viewModels()
    private var panelBinding: ViewAddCategoriesPanelBinding? = null
    private var selectedDate: Long = System.currentTimeMillis()

    private val adapter = AddExpenseOperationAdapter { selectedCategory ->
        showPanel(selectedCategory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.rvAddExpenseOperation?.adapter = null
        panelBinding = null
        _binding = null
    }

    private fun setupAdapter() = with(binding) {
        rvAddExpenseOperation.layoutManager = GridLayoutManager(requireContext(), 4)
        rvAddExpenseOperation.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.expenseCategories.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    private fun showPanel(categoryName: CategoryUI) {
        binding.bottomInputContainer.visibility = View.VISIBLE

        if (panelBinding == null) {
            panelBinding = ViewAddCategoriesPanelBinding.inflate(
                layoutInflater,
                binding.bottomInputContainer,
                true
            )
            setupPanelListeners()
        }

        panelBinding?.apply {
            tvTitlePanel.text = context?.getString(
                CategoryKeys.getCategoryNameRes(categoryName.categoryName)
            ) ?: categoryName.categoryName

            edtNote.text?.clear()
            edtDate.setText(formatDate(selectedDate))
            edtAmount.setText("0")
            tilAmount.error = null

            imAddNewOperation.setOnClickListener {
                validateAndSave(categoryName)
            }
        }
    }

    private fun setupPanelListeners() = panelBinding?.apply {

        edtAmount.setupAmountLogic()

        edtAmount.filters = arrayOf(
            DecimalDigitsInputFilter(
                Constants.DigitFilter.DIGITS_BEFORE_ZERO,
                Constants.DigitFilter.DIGITS_AFTER_ZERO,
            )
        )
        setupDatePicker()
    }

    private fun validateAndSave(categoryName: CategoryUI) = panelBinding?.apply {
        val amountText = edtAmount.text.toString().replace(",", ".")
        val amountDouble = amountText.toDoubleOrNull() ?: 0.0

        if (amountDouble <= 0.0) {
            edtAmount.setText("0")
            tilAmount.error = context?.getString(R.string.tilAmountOperationError)
            return@apply
        }

        val noteText = edtNote.text.toString()
        viewModel.addExpenseOperation(categoryName, amountDouble, noteText, selectedDate)
        findNavController().navigate(R.id.mainActivity)
    }

    private fun setupDatePicker() {
        panelBinding?.edtDate?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar.timeInMillis
                    panelBinding?.edtDate?.setText(formatDate(selectedDate))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    }
}