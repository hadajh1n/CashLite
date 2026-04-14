package com.example.cashlite.ui.fragment.newOperation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cashlite.R
import com.example.cashlite.core.utils.Constants
import com.example.cashlite.core.utils.format.DecimalDigitsInputFilter
import com.example.cashlite.core.utils.format.formatDate
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.databinding.FragmentAddTransferOperationBinding
import com.example.cashlite.ui.viewModel.newOperation.AddTransferViewModel
import java.util.Calendar

class AddTransferOperationFragment : Fragment() {

    private var _binding: FragmentAddTransferOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddTransferViewModel by viewModels()
    private var selectedCategoryKey: String = CategoryKeys.TRANSFER_EXPENSE
    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTransferOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChipGroup()
        onButtonAddTransfer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupChipGroup() = with(binding) {
        chipGroupType.setOnCheckedChangeListener { _, checkedId ->
            selectedCategoryKey = when (checkedId) {
                R.id.chipExpense -> CategoryKeys.TRANSFER_EXPENSE
                R.id.chipIncome -> CategoryKeys.TRANSFER_INCOME
                else -> CategoryKeys.TRANSFER_EXPENSE
            }
        }
    }

    private fun onButtonAddTransfer() = with(binding) {
        edtAmount.filters = arrayOf(
            DecimalDigitsInputFilter(
                Constants.DigitFilter.DIGITS_BEFORE_ZERO,
                Constants.DigitFilter.DIGITS_AFTER_ZERO,
            )
        )

        setupDatePicker()

        btnAddTransfer.setOnClickListener {
            val contactText = edtContact.text.toString()
            val amountText = edtAmount.text.toString()
            val noteText = edtNote.text.toString()
            edtDate.setText(formatDate(selectedDate))

            if (contactText.isBlank()) {
                tilContact.error = context?.getString(R.string.tilContactOperationTransferError)
                return@setOnClickListener
            } else {
                tilContact.error = null
            }

            if (amountText.isBlank()) {
                tilAmount.error = context?.getString(R.string.tilAmountOperationTransferError)
                return@setOnClickListener
            } else {
                tilAmount.error = null
            }

            val amountDouble = amountText.toDoubleOrNull()

            if (amountDouble == null) {
                tilAmount.error = context?.getString(R.string.tilAmountNullError)
                return@setOnClickListener
            }

            viewModel.addOperationTransfer(
                selectedCategoryKey, amountDouble, noteText, selectedDate, contactText
            )

            findNavController()
                .navigate(R.id.mainActivity)
        }
    }

    private fun setupDatePicker() = with(binding) {
        edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val dialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)

                    selectedDate = selectedCalendar.timeInMillis

                    edtDate.setText(
                        formatDate(selectedDate)
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            dialog.show()
        }
    }
}