package com.example.expense_manager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.expense_manager.Data.ExpenseViewModel
import com.example.expense_manager.Data.Transaction
import com.example.expense_manager.Data.User
import com.example.expense_manager._addScreen
import com.example.expense_manager._homeScreen
import com.example.expense_manager._profileScreen
import com.example.expense_manager._viewTransactionScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: ExpenseViewModel) {


    val userDataState = viewModel.observeUser().collectAsStateWithLifecycle(initialValue = null)
    val transactionsState = viewModel.observeTransactions().collectAsStateWithLifecycle(initialValue = emptyList())
    val userData = userDataState.value ?: User()

    fun calulateALLTransactions(transactions: List<Transaction>): Double{
        var totalExpense=0.0
        for(transaction in transactions){
            if(transaction.type=="Expense"){
                totalExpense+=transaction.amount
            }
        }
        return totalExpense

    }
    var fixed = if (userData.budget == 0.0) 0.0 else userData.budget




    var variable = remember { mutableStateOf(0) }
    var indicatorVal = remember { mutableStateOf(0f) }
    val alert = remember { mutableStateOf(false) }
    val alertText = remember { mutableStateOf("") }

    Scaffold() { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            topAppBar("Expense Manager",navController)

            Spacer(Modifier.height(100.dp))
            variable.value = calulateALLTransactions(transactionsState.value).toInt()

            indicatorVal.value = if (fixed > 0.0) {
                (variable.value / fixed.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { indicatorVal.value },
                    modifier = Modifier.size(150.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 15.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                // Text inside the progress bar
                Text(
                    "${variable.value}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(20.dp))


            if (userData.budget == 0.0) {
                Button(
                    onClick = { alert.value = true },
                    modifier = Modifier.height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Text(
                        text = "Set Budget",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (alert.value) {
                    AlertDialog(
                        onDismissRequest = { alert.value = false },
                        title = { Text("Set Budget") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = alertText.value,
                                    onValueChange = { alertText.value = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                val budgetValue = alertText.value.toDoubleOrNull()
                                if (budgetValue != null) {
                                    viewModel.updateBudget(budgetValue)
                                    alert.value = false
                                }
                                navController.navigate(_homeScreen)
                            }) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { alert.value = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            } else {
                Text(
                    text = "Budget: $fixed",
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }




            Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Button(
                    onClick = { navController.navigate(_addScreen) },
                    modifier = Modifier.height(55.dp).width(280.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Text(
                        text = "Add Transaction",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        navController.navigate(_viewTransactionScreen)
                    },
                    modifier = Modifier.height(55.dp).width(280.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Text(
                        text = "View Transactions",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(100.dp))



        }
    }

}





@Composable
fun topAppBar(name: String,navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(onClick = {navController.navigate(_profileScreen)}) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
            }
        }

    }
}



