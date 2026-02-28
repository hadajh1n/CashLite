package com.example.cashlite.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.IconIncomeCategory
import com.example.cashlite.databinding.FragmentAddIncomeOperationBinding
import com.example.cashlite.databinding.ViewAddCategoriesPanelBinding
import com.example.cashlite.ui.adapter.AddIncomeOperationAdapter
import com.example.cashlite.ui.viewModel.AddIncomeViewModel
import kotlin.getValue

class AddIncomeOperationFragment : Fragment() {

    private var _binding: FragmentAddIncomeOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddIncomeViewModel by viewModels()
    private var panelBinding: ViewAddCategoriesPanelBinding? = null

    private val adapter = AddIncomeOperationAdapter { selectedCategory ->
        showPanel(selectedCategory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddIncomeOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initIncomeCategory()

        setupAdapter()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        panelBinding = null
    }

    private fun setupAdapter() = with(binding) {
        rvAddIncomeOperation.layoutManager = GridLayoutManager(requireContext(), 4)
        rvAddIncomeOperation.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.incomeCategories.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    private fun showPanel(categoryName: IconIncomeCategory) {

        binding.bottomInputContainer.visibility = View.VISIBLE

        if (panelBinding == null) {
            panelBinding = ViewAddCategoriesPanelBinding.inflate(
                layoutInflater,
                binding.bottomInputContainer,
                true
            )
        }

        panelBinding?.apply {

            tvTitlePanel.text = categoryName.categoryName
            etAmount.text?.clear()
            etNote.text?.clear()

            imAddNewOperation.setOnClickListener {
                val amountText = etAmount.text.toString()
                val noteText = etNote.text.toString()
                val amountDouble = amountText.toDoubleOrNull()

                if (amountDouble != null) {
                    viewModel.addIncomeOperation(categoryName, amountDouble, noteText)
                    findNavController()
                        .navigate(R.id.mainActivity)
                }
            }
        }
    }
}