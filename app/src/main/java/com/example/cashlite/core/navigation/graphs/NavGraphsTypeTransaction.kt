//package com.example.cashlite.core.navigation.graphs
//
//import androidx.fragment.app.Fragment
//import android.view.View
//import androidx.lifecycle.DefaultLifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.navigation.NavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.navOptions
//import com.example.cashlite.R
//import com.example.cashlite.databinding.FragmentMainGraphsBinding
//
//fun Fragment.setupGraphsTypeTransactionNav(
//    binding: FragmentMainGraphsBinding
//) = with(binding) {
//
//    val navHostFragment = childFragmentManager
//        .findFragmentById(R.id.nav_main_graphs_type) as? NavHostFragment
//        ?: return@with
//
//    val navController = navHostFragment.navController
//
//    fun selectButton(selected: View) {
//        btnExpense.isSelected = false
//        btnIncome.isSelected = false
//        selected.isSelected = true
//    }
//
//    fun navigateSafe(destinationId: Int) {
//        if (navController.currentDestination?.id == destinationId) return
//
//        navController.navigate(destinationId, null, navOptions {
//            launchSingleTop = true
//        })
//    }
//
//    val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
//        when (destination.id) {
//            R.id.graphsExpenseFragment -> selectButton(btnExpense)
//            R.id.graphsIncomeFragment -> selectButton(btnIncome)
//        }
//    }
//
//    navController.addOnDestinationChangedListener(listener)
//
//    btnExpense.setOnClickListener {
//        navigateSafe(R.id.graphsExpenseFragment)
//    }
//
//    btnIncome.setOnClickListener {
//        navigateSafe(R.id.graphsIncomeFragment)
//    }
//
//    viewLifecycleOwner.lifecycle.addObserver(
//        object : DefaultLifecycleObserver {
//            override fun onDestroy(owner: LifecycleOwner) {
//                navController.removeOnDestinationChangedListener(listener)
//            }
//        }
//    )
//}