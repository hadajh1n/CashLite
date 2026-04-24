package com.example.cashlite.ui.fragment.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cashlite.R
import com.example.cashlite.core.utils.format.formatDate
import com.example.cashlite.core.utils.format.formatMoney
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.room.category.CategoryType
import com.example.cashlite.databinding.DialogConfirmationDeleteTransactionBinding
import com.example.cashlite.databinding.FragmentDetailsOperationBinding
import com.example.cashlite.ui.viewModel.details.DetailsViewModel

class DetailsOperationFragment : Fragment() {

    private var _binding: FragmentDetailsOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var currentTransaction: TransactionUI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTransaction()
        setupBackButton()
        setupUI()
        setupDeleteButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initTransaction() {
        currentTransaction = requireActivity()
            .intent
            .getParcelableExtra("transaction")
            ?: throw IllegalStateException("TransactionUI не передан")
    }

    private fun setupBackButton() = with(binding) {
        imBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupUI() = with(binding) {
        imCategoryDetails.setImageResource(currentTransaction.imageId)
        imCategoryDetails.setColorFilter(requireContext().getColor(currentTransaction.color))

        val isUnknown = currentTransaction.categoryName == CategoryKeys.UNKNOWN_EXPENSE ||
                currentTransaction.categoryName == CategoryKeys.UNKNOWN_INCOME

        tvCategoryDetails.text = if (isUnknown) {
            getString(R.string.unknown_general)
        } else {
            getString(CategoryKeys.getCategoryNameRes(currentTransaction.categoryName))
        }

        tvAmountDetails.text = when (currentTransaction.type) {
            CategoryType.EXPENSE -> "-${currentTransaction.amount.formatMoney()} ₽"
            CategoryType.INCOME  -> "+${currentTransaction.amount.formatMoney()} ₽"
            else                 -> "${currentTransaction.amount.formatMoney()} ₽"
        }

        tvDateDetails.text = formatDate(currentTransaction.date)
        tvTypeDetails.text =
            if (currentTransaction.type == CategoryType.EXPENSE) "Расход" else "Доход"

        setupOptionalField(llContactDetails, tvContactDetails, currentTransaction.contact)
        setupOptionalField(llNoteDetails, tvNoteDetails, currentTransaction.note)
    }

    private fun setupOptionalField(
        container: LinearLayout,
        valueView: TextView,
        value: String?
    ) {
        if (value.isNullOrBlank()) {
            container.visibility = View.GONE
        } else {
            container.visibility = View.VISIBLE
            valueView.text = value
        }
    }

    private fun setupDeleteButton() = with(binding) {
        btnDeleteOperation.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBinding = DialogConfirmationDeleteTransactionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()

        dialogBinding.btnConfirmationDeleteTransaction.setOnClickListener {
            viewModel.onRemoveTransaction(currentTransaction.idTransaction)
            requireActivity().finish()
        }

        dialogBinding.btnCancelDeleteTransaction.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}