package com.example.expense_manager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_manager.Data.ExpenseViewModel
import com.example.expense_manager.Data.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ViewTransaction (){
    val viewModel: ExpenseViewModel = viewModel()
    val transactionList = viewModel.observeTransactions().collectAsStateWithLifecycle(initialValue = emptyList()).value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top

    ) {

        Spacer(Modifier.height(30.dp))
        Text("Your Transactions",
            modifier = Modifier
                .padding(start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(10.dp))
        LazyColumn{
            items(transactionList){ item->
                viewTransaction(item)
            }
        }
    }
}

@Composable
fun viewTransaction(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(15.dp),
        shape = RoundedCornerShape(10.dp),
//        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.type == "Expense")
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = transaction.amount.toString(),
                    modifier = Modifier.padding(end = 10.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == "Expense")
                        MaterialTheme.colorScheme.onErrorContainer
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = transaction.type,
                    fontWeight = FontWeight.Medium,
                    color = if (transaction.type == "Expense")
                        MaterialTheme.colorScheme.onErrorContainer
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = transaction.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = if (transaction.type == "Expense")
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(4.dp))
            val formattedDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date(transaction.createAt))
            Text(
                text = formattedDate,
                fontSize = 12.sp,
                color = if (transaction.type == "Expense")
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
