package com.example.expense_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expense_manager.Data.ExpenseViewModel
import com.example.expense_manager.screen.AddScreen
import com.example.expense_manager.screen.HomeScreen
import com.example.expense_manager.screen.ProfileScreen
import com.example.expense_manager.screen.ViewTransaction
import com.example.expense_manager.ui.theme.Expense_ManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Expense_ManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController= rememberNavController()
    val viewModel: ExpenseViewModel = viewModel()


    NavHost(navController, startDestination = _homeScreen) {
        composable(_homeScreen) {
            HomeScreen(navController,viewModel)
        }
        composable(_addScreen) {
            AddScreen(navController,viewModel)
        }
        composable(_viewTransactionScreen) {
            ViewTransaction()
        }
        composable(_profileScreen) {
            ProfileScreen(navController,viewModel)
        }

    }
}