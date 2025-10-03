package com.example.expense_manager.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository =
        ExpenseRepository(AppDatabase.getInstance(application).expenseDao())

    // Simple local identity replacing Firebase Auth
    val currentUserId: String = "local-user"
    val currentUserName: String = "Expense Manager"

    init {
        viewModelScope.launch {
            repository.ensureUser(currentUserId, currentUserName)
        }
    }

    fun observeUser(): Flow<User?> = repository.observeUser(currentUserId)

    fun observeTransactions(): Flow<List<Transaction>> = repository.observeTransactions(currentUserId)

    fun updateBudget(budget: Double) {
        viewModelScope.launch { repository.updateBudget(currentUserId, budget) }
    }

    fun addTransaction(amount: Double, type: String, title: String) {
        viewModelScope.launch { repository.addTransaction(currentUserId, amount, type, title) }
    }
}