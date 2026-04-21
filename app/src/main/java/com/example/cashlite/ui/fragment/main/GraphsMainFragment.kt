package com.example.cashlite.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cashlite.core.navigation.graphs.setupGraphsTypeTransactionNav
import com.example.cashlite.databinding.FragmentMainGraphsBinding

class GraphsMainFragment : Fragment() {

    private var _binding: FragmentMainGraphsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainGraphsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGraphsTypeTransactionNav(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}