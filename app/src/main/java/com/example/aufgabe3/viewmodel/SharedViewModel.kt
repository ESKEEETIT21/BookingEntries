package com.example.aufgabe3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aufgabe3.model.BookingEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class SharedViewModel: ViewModel() {
    private val _bookingsEntries = MutableStateFlow<List<BookingEntry>>(emptyList())
    val bookingsEntries: StateFlow<List<BookingEntry>> = _bookingsEntries

    /**
     * Adds a new booking entry to the list of bookings.
     *
     * @param name The name of the person making the booking.
     * @param arrivalDate The arrival date for the booking.
     * @param departureDate The departure date for the booking.
     */
    fun addBookingEntry(name: String, arrivalDate: LocalDate, departureDate: LocalDate) {
        // TODO create a new booking entry and save it
        val newBookingEntry = BookingEntry(name, arrivalDate, departureDate)
        val updatedList = _bookingsEntries.value + newBookingEntry
        _bookingsEntries.value = updatedList
    }

    /**
     * Deletes an existing booking entry from the list of bookings.
     *
     * @param bookingEntry The booking entry to be removed.
     */
    fun deleteBookingEntry(bookingEntry: BookingEntry) {
        // TODO delete a new booking entry
        val updatedList = _bookingsEntries.value.minus(bookingEntry)
        _bookingsEntries.value = updatedList
    }
}