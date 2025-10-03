package com.example.expense_manager.Data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Long = 0,
    val userId: String = "",
    val createAt: Long = System.currentTimeMillis(),
    val amount: Double = 0.0,
    val type: String = "",
    val title: String = ""
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "",
    val name: String = "Expense Manager",
    val budget: Double = 0.0
)