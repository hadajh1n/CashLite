//package com.example.cashlite.ui.viewModel.main
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.cashlite.data.repository.AppRepository
//import kotlinx.coroutines.launch
//
//class MainViewModel : ViewModel() {
//
//    private var isInitSystemCategories = false
//
//    fun initSystemCategories() {
//        if (isInitSystemCategories) {
//            Log.e("TEST_WORKING", "Системные категории уже проинициализированы")
//            return
//        }
//
//        isInitSystemCategories = true
//
//        viewModelScope.launch {
//            Log.e("TEST_WORKING", "Запрос инициализации системных категорий")
//            AppRepository.initCategories()
//        }
//    }
//}