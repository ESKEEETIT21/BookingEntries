package com.example.aufgabe3.ui.add

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Composable function for adding a new booking entry.
 *
 * @param navController The NavHostController to handle navigation actions.
 * @param sharedViewModel The shared ViewModel to manage booking entries.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    var name by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf<LocalDate?>(null) }
    var departureDate by remember { mutableStateOf<LocalDate?>(null) }

    var showDateRangePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Booking Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (arrivalDate != null && departureDate != null) {
                    "${arrivalDate!!.format(dateFormatter)} - ${departureDate!!.format(dateFormatter)}"
                } else {
                    ""
                },
                onValueChange = {},
                label = { Text("Select Date Range") },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO Error handling and creating new BookingEntry and save in sharedViewModel
                    if(validateBookingEntry(name, arrivalDate, departureDate)) {
                        sharedViewModel.addBookingEntry(name, arrivalDate!!, departureDate!!)
                        navController.popBackStack()
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Invalid Booking Entry",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
    // TODO implement DateRangePicker Dialog logic
    if (showDateRangePicker) {
        DateRangePickerModal(
            onDismiss = { showDateRangePicker = false },
            onRangeSelected = { dateRange ->
                val startDate = dateRange.first
                val endDate = dateRange.second
                arrivalDate = startDate?.let { convertDate(it) }
                departureDate = endDate?.let { convertDate(it) }
                showDateRangePicker = false
            },

            validDate = { dateToValidate ->
                !convertDate(dateToValidate).isBefore(LocalDate.now())
            })
    }
}

/**
 * Validates the booking entry to ensure all fields are properly filled.
 *
 * @param name The name of the person making the booking.
 * @param arrivalDate The arrival date of the booking.
 * @param departureDate The departure date of the booking.
 * @return True if the booking entry is valid (all fields are non-null and non-empty), false otherwise.
 */
fun validateBookingEntry(name: String, arrivalDate: LocalDate?, departureDate: LocalDate?) : Boolean {
    return !(null == arrivalDate || null == departureDate || name.isNullOrEmpty())
}

/**
 * Converts a timestamp (in milliseconds) to a LocalDate.
 *
 * @param timestamp The timestamp in milliseconds to be converted.
 * @return The corresponding LocalDate derived from the timestamp.
 */
fun convertDate(timestamp: Long): LocalDate {
    return timestamp.let {
        LocalDateTime.ofEpochSecond(
            it / 1000, 0, ZoneId.systemDefault().rules.getOffset(LocalDateTime.now())
        ).toLocalDate()
    }
}

/**
 * Composable function for displaying a date range picker dialog.
 *
 * @param onRangeSelected A lambda function that is invoked when a date range is selected, passing the start and end date in milliseconds.
 * @param onDismiss A lambda function to be called when the dialog is dismissed.
 * @param validDate A function to validate whether a date is valid, given as a timestamp in milliseconds.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit,
    validDate: (Long) -> Boolean
) {
    // TODO implement DateRangePicker see https://developer.android.com/develop/ui/compose/components/datepickers?hl=de
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onRangeSelected(
                        Pair(dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss
                }
            ) {
                Text(
                    text = "Confirm"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel"
                )
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select Date Range"
                )
            },
            showModeToggle = false,
            dateValidator = {validDate(it)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(500.dp)
        )
    }
}