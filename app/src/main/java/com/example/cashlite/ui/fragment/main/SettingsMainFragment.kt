package com.example.cashlite.ui.fragment.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentMainSettingsBinding
import com.example.cashlite.ui.viewModel.main.SettingsViewModel

class SettingsMainFragment : Fragment() {

    private var _binding: FragmentMainSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    private var languageDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {

        switchTheme.isChecked = viewModel.isDarkMode()

        switchTheme.setOnClickListener {
            val isChecked = switchTheme.isChecked
            viewModel.toggleTheme(isChecked)
        }

        updateLanguageText()
        cvLanguageSettings.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showLanguageDialog() {
        val langs = arrayOf("Русский", "English")
        val codes = arrayOf("ru", "en")

        languageDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tvTitleLanguageSettings))
            .setItems(langs) { dialog, which ->
                viewModel.setLanguage(codes[which])
                dialog.dismiss()
            }
            .create()

        languageDialog?.show()
    }

    private fun updateLanguageText() {
        _binding?.let {
            val langName = when (viewModel.getCurrentLanguageCode()) {
                "ru" -> "Русский"
                "en" -> "English"
                else -> "Русский"
            }
            it.tvCurrentLanguageSettings.text = langName
        }
    }

    override fun onDestroyView() {
        languageDialog?.dismiss()
        languageDialog = null

        _binding?.let {
            it.switchTheme.setOnClickListener(null)
            it.cvLanguageSettings.setOnClickListener(null)
        }

        super.onDestroyView()
        _binding = null
    }
}