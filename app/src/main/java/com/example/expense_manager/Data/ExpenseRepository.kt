package com.example.expense_manager.Data

import kotlinx.coroutines.flow.Flow


class ExpenseRepository(private val dao: ExpenseDao) {

    fun observeUser(userId: String): Flow<User?> = dao.observeUser(userId)

    suspend fun ensureUser(userId: String, name: String) {
        val existing = dao.getUserOnce(userId)
        if (existing == null) {
            dao.upsertUser(User(id = userId, name = name, budget = 0.0))
        }
    }

    suspend fun updateBudget(userId: String, budget: Double) {
        dao.updateBudget(userId, budget)
    }

    fun observeTransactions(userId: String): Flow<List<Transaction>> = dao.observeTransactions(userId)

    suspend fun addTransaction(userId: String, amount: Double, type: String, title: String) {
        dao.insertTransaction(
            Transaction(
                userId = userId,
                amount = amount,
                type = type,
                title = title,
                createAt = System.currentTimeMillis()
            )
        )
    }
}