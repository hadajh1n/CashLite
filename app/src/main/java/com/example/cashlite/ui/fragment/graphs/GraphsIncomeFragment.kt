package com.example.cashlite.ui.fragment.graphs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentGraphsIncomeBinding

class GraphsIncomeFragment : Fragment() {

    private var _binding: FragmentGraphsIncomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphsIncomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}