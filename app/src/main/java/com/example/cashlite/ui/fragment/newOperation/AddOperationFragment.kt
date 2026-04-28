package com.example.cashlite.ui.fragment.newOperation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cashlite.R
import com.example.cashlite.core.navigation.setupAddOperationNav
import com.example.cashlite.databinding.DialogDuplicateImportBinding
import com.example.cashlite.databinding.FragmentAddOperationBinding
import com.example.cashlite.ui.viewModel.newOperation.AddOperationViewModel
import com.example.cashlite.ui.viewModel.newOperation.ImportUiState

class AddOperationFragment : Fragment() {

    private var _binding: FragmentAddOperationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddOperationViewModel by viewModels()

    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                viewModel.importPdf(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddOperationNav(binding)
        setupBackButton()
        observeImportResult()
        observeImportState()
        onButtonImport()
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

    private fun observeImportResult() {
        viewModel.importSuccess.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(
                    requireContext(),
                    R.string.observeImportSuccess,
                    Toast.LENGTH_LONG
                ).show()

                findNavController().navigate(R.id.mainActivity)
            } else if (success == false) {
                Toast.makeText(
                    requireContext(),
                    R.string.observeImportError,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun observeImportState() = with(binding) {
        viewModel.importState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ImportUiState.Loading -> {
                    progressBarForImport.visibility = View.VISIBLE
                    imBack.visibility = View.GONE
                    btnImport.visibility = View.GONE
                    topNavigationOperation.visibility = View.GONE
                    navHostCategoryOperationFragment.visibility = View.GONE
                }
                ImportUiState.Standard -> {
                    progressBarForImport.visibility = View.GONE
                    imBack.visibility = View.VISIBLE
                    btnImport.visibility = View.VISIBLE
                    topNavigationOperation.visibility = View.VISIBLE
                    navHostCategoryOperationFragment.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onButtonImport() = with(binding) {
        btnImport.setOnClickListener {
            viewModel.checkCanImport { canImport ->
                if (canImport) {
                    pickPdfLauncher.launch(arrayOf("application/pdf"))
                } else {
                    showDuplicateImport()
                }
            }
        }
    }

    private fun showDuplicateImport() {
        val dialogBinding = DialogDuplicateImportBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnCancelDialogDuplicateImport.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}