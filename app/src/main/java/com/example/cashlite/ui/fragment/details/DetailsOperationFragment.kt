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
import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.room.category.CategoryType
import com.example.cashlite.databinding.FragmentDetailsOperationBinding
import com.example.cashlite.ui.viewModel.details.DetailsViewModel

class DetailsOperationFragment : Fragment() {

    private var _binding: FragmentDetailsOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

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

        setupBackButton()
        setupUI()
        onDeleteTransaction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBackButton() = with(binding) {
        imBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupUI() = with(binding) {
        val transaction: TransactionUI = requireActivity()
            .intent
            .getParcelableExtra("transaction")
            ?: throw IllegalStateException("TransactionUI не передан в DetailsOperationActivity")

        imCategoryDetails.setImageResource(transaction.imageId)
        imCategoryDetails.setColorFilter(requireContext().getColor(transaction.color))

        val isUnknown = transaction.categoryName == CategoryKeys.UNKNOWN_EXPENSE ||
                transaction.categoryName == CategoryKeys.UNKNOWN_INCOME

        tvCategoryDetails.text = if (isUnknown) {
            getString(R.string.unknown_general)
        } else {
            getString(CategoryKeys.getCategoryNameRes(transaction.categoryName))
        }

        tvAmountDetails.text = when (transaction.type) {
            CategoryType.EXPENSE -> "-${transaction.amount.formatMoney()} ₽"
            CategoryType.INCOME  -> "+${transaction.amount.formatMoney()} ₽"
            else                 -> "${transaction.amount.formatMoney()} ₽"
        }

        tvDateDetails.text = formatDate(transaction.date)
        tvTypeDetails.text = if (transaction.type == CategoryType.EXPENSE) "Расход" else "Доход"

        setupOptionalField(llContactDetails, tvContactDetails, transaction.contact)
        setupOptionalField(llNoteDetails, tvNoteDetails, transaction.note)
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

    private fun onDeleteTransaction() = with(binding) {
        btnDeleteOperation.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val transaction = requireActivity()
            .intent
            .getParcelableExtra<TransactionUI>("transaction")
            ?: return

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialogDeleteTransactionTitle)
            .setMessage(R.string.dialogDeleteTransactionMessage)
            .setPositiveButton(R.string.dialogButtonYes) { _, _ ->
                viewModel.onRemoveTransaction(transaction.idTransaction)
                requireActivity().finish()
            }
            .setNegativeButton(R.string.dialogButtonNo, null)
            .show()
    }
}