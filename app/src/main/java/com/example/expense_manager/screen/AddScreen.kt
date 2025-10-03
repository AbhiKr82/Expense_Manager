package com.example.expense_manager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.expense_manager._homeScreen
import com.example.expense_manager._profileScreen


@Composable
fun AddScreen(navController: NavHostController, viewModel: ExpenseViewModel) {


    val userData = viewModel.observeUser().collectAsStateWithLifecycle(initialValue = null)



    var title = remember { mutableStateOf("") }
    var amount = remember { mutableStateOf("") }
    var type = remember { mutableStateOf("") }
    val showAlert = remember { mutableStateOf(false) }
    val alertText = remember { mutableStateOf("") }
    val navigateToProfileOnConfirm = remember { mutableStateOf(false) }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(text = "Add Transaction",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { type.value = "Expense" },
                modifier = Modifier.height(50.dp).width(120.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type.value == "Expense")
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "Expense",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (type.value == "Expense")
                        MaterialTheme.colorScheme.onErrorContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = { type.value = "Income" },
                modifier = Modifier.height(50.dp).width(120.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type.value == "Income")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "Income",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (type.value == "Income")
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") }
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(onClick = {
                navController.navigate(_homeScreen)
            },
                modifier = Modifier.height(50.dp).width(120.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(text = "Cancel",fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(onClick = {
                val enteredAmount = amount.value.toDoubleOrNull() ?: return@Button
                if (enteredAmount < 0) {
                    alertText.value = "Amount cannot be negative"
                    navigateToProfileOnConfirm.value = false
                    showAlert.value = true
                    return@Button
                }
                if (type.value.isEmpty() || title.value.isEmpty()) return@Button

                val budget = userData.value?.budget ?: 0.0
                if (type.value == "Expense" && enteredAmount > budget) {
                    alertText.value = "Expense exceeds your budget. Please update budget from Profile."
                    navigateToProfileOnConfirm.value = true
                    showAlert.value = true
                    return@Button
                }

                viewModel.addTransaction(
                    amount = enteredAmount,
                    type = type.value,
                    title = title.value
                )
                navController.popBackStack()
            },
                modifier = Modifier.height(50.dp).width(120.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Add",fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (showAlert.value) {
            AlertDialog(
                onDismissRequest = { showAlert.value = false },
                title = { Text("Notice") },
                text = { Text(alertText.value) },
                confirmButton = {
                    Button(onClick = {
                        val shouldNavigate = navigateToProfileOnConfirm.value
                        showAlert.value = false
                        if (shouldNavigate) {
                            navController.navigate(_profileScreen)
                        }
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showAlert.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}