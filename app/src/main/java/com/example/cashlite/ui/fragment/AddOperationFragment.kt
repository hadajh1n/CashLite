package com.example.cashlite.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cashlite.R
import com.example.cashlite.core.navigation.setupAddOperationNavigation
import com.example.cashlite.databinding.FragmentAddOperationBinding

class AddOperationFragment : Fragment() {

    private var _binding: FragmentAddOperationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOperationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddOperationNavigation(binding)
        setupBackButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBackButton() {
        binding.imBack.setOnClickListener {
            findNavController()
                .navigate(R.id.mainActivity)
        }
    }
}