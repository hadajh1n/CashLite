package com.example.cashlite.ui.fragment

import com.example.cashlite.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cashlite.data.dataclass.ExpenceIcons
import com.example.cashlite.databinding.FragmentAddExpenceOperationBinding
import com.example.cashlite.ui.adapter.AddExpenceOperationAdapter

class AddExpenceOperationFragment : Fragment() {

    private var _binding: FragmentAddExpenceOperationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AddExpenceOperationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddExpenceOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AddExpenceOperationAdapter { }

        setupAdapter()
        submitIcons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter() = with(binding) {
        rvAddExpenceOperation.layoutManager = GridLayoutManager(requireContext(), 4)
        rvAddExpenceOperation.adapter = adapter
    }

    private fun submitIcons() {
        val icons = listOf(
            ExpenceIcons(R.drawable.icon_food, "Еда"),
            ExpenceIcons(R.drawable.icon_clothes, "Одежда"),
            ExpenceIcons(R.drawable.icon_car, "Автомобиль"),
            ExpenceIcons(R.drawable.icon_bicycle, "Спорт"),
            ExpenceIcons(R.drawable.icon_education, "Образование"),
            ExpenceIcons(R.drawable.icon_flag, "Путешествия"),
            ExpenceIcons(R.drawable.icon_laptop, "Электроника"),
            ExpenceIcons(R.drawable.icon_phone, "Телефон"),
        )
        adapter.submitList(icons)
    }
}