package com.example.aufgabe3.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Composable function that displays the home screen with a list of booking entries.
 * It allows navigation to the add booking screen and deletion of booking entries.
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param sharedViewModel The shared ViewModel to manage and access booking entries.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val bookingsEntries by sharedViewModel.bookingsEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Entries") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add booking")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // TODO inform the user if no bookingsEntries otherwise LazyColumn for bookingsEntries
            if (bookingsEntries.isNotEmpty()) {
                LazyColumn {
                    items(bookingsEntries) { bookingEntry ->
                        BookingEntryItem(bookingEntry) {
                            sharedViewModel.deleteBookingEntry(bookingEntry)
                        }
                    }
                }
            } else {
                Text(
                    text = "No booking Entries available",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textAlign = TextAlign.Left,

                )
            }
        }
    }
}

/**
 * Composable function that displays a single booking entry item in a card format.
 * It shows the booking name and date range, with a delete button to remove the entry.
 *
 * @param booking The booking entry to be displayed.
 * @param onDeleteClick A lambda function that is invoked when the delete button is clicked.
 */
@Composable
fun BookingEntryItem(
    booking: BookingEntry,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = booking.name, // TODO display booking name,
                    style = MaterialTheme.typography.titleMedium
                )
                val formatted = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(
                    Locale.getDefault()).withZone(ZoneId.systemDefault())
                val date = "${booking.arrivalDate.format(formatted)} - ${booking.departureDate.format(formatted)}"
                Text(
                    text = date, // TODO display date in right format,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete booking")
            }
        }
    }
}