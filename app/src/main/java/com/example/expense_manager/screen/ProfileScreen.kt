package com.example.expense_manager.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.expense_manager._profileScreen

@Composable
fun ProfileScreen(navController: NavHostController , viewModel: ExpenseViewModel) {

    val userDataState = viewModel.observeUser().collectAsStateWithLifecycle(initialValue = null)
    val userData = userDataState.value ?: User()

    var alert = remember { mutableStateOf(false) }
    var alertText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) { }
    fun calulateALLIncome(transactions: List<Transaction>): Double{
        var totalIncome=0.0
        for(transaction in transactions){
            if(transaction.type=="Income"){
                totalIncome+=transaction.amount
            }
        }
        return totalIncome

    }
    val transactionsState = viewModel.observeTransactions().collectAsStateWithLifecycle(initialValue = emptyList())
    var Income= calulateALLIncome(transactionsState.value)


    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(Modifier.height(100.dp))

        Icon(
            Icons.Default.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(20.dp))

        Text(text = " ${userData.name}"
            ,modifier = Modifier.padding(start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(15.dp))
        Text(text = "Budget:-  ${userData.budget}",
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(30.dp))
        Text(text = "Extra Income:- $Income",
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )


        Spacer(Modifier.height(200.dp))

        alertText.value= userData.budget.toString()

        Button(
            onClick = {
                alert.value = true
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(55.dp),
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
                text = "Update Budget",
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
                    Button(
                        onClick = {
                            val budgetValue = alertText.value.toDoubleOrNull()
                            if (budgetValue != null) {
                                viewModel.updateBudget(budgetValue)
                                alert.value = false
                            }
                            navController.navigate(_profileScreen)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { alert.value = false },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            contentAlignment = Alignment.BottomCenter

        ) {


        }
    }

}