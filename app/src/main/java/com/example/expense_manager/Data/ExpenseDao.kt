package com.example.expense_manager.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    // User operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeUser(userId: String): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserOnce(userId: String): User?

    @Query("UPDATE users SET budget = :budget WHERE id = :userId")
    suspend fun updateBudget(userId: String, budget: Double)

    // Transactions operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(txn: Transaction)

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY createAt DESC")
    fun observeTransactions(userId: String): Flow<List<Transaction>>

    @Query("DELETE FROM transactions WHERE userId = :userId")
    suspend fun clearTransactionsForUser(userId: String)
}