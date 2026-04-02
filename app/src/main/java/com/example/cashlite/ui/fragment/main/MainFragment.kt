package com.example.cashlite.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.cashlite.core.navigation.setupBottomNav
import com.example.cashlite.databinding.FragmentMainBinding
import com.example.cashlite.ui.viewModel.main.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSystemCategories()
        setupBottomNav(binding)
    }

    private fun getSystemCategories() {
        viewModel.initSystemCategories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}