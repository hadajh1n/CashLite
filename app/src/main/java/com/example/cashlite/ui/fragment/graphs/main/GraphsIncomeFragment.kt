package com.example.cashlite.ui.fragment.graphs.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cashlite.core.navigation.graphs.setupGraphsIncomeSortNav
import com.example.cashlite.databinding.FragmentGraphsIncomeBinding

class GraphsIncomeFragment : Fragment() {

    private var _binding: FragmentGraphsIncomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphsIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGraphsIncomeSortNav(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}