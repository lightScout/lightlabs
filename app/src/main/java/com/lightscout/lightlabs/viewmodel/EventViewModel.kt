package com.lightscout.lightlabs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lightscout.lightlabs.data.Event
import com.lightscout.lightlabs.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class EventViewModel @Inject constructor(private val eventRepository: EventRepository) :
        ViewModel() {

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Observe all active events from the database
    private val events: StateFlow<List<Event>> =
            eventRepository
                    .getAllActiveEvents()
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = emptyList()
                    )

    // Filter events by category
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredEvents: StateFlow<List<Event>> =
            combine(events, selectedCategory) { eventList, category ->
                        if (category == "All") {
                            eventList
                        } else {
                            eventList.filter { it.category == category }
                        }
                    }
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = emptyList()
                    )

    val categories: StateFlow<List<String>> =
            events
                    .map { eventList ->
                        listOf("All") + eventList.map { it.category }.distinct().sorted()
                    }
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = listOf("All")
                    )

    init {
        initializeSampleData()
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun incrementAttendeeCount(eventId: Long) {
        viewModelScope.launch {
            try {
                val event = eventRepository.getEventById(eventId)
                event?.let { eventRepository.updateAttendeeCount(eventId, it.attendeeCount + 1) }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update attendee count: ${e.message}"
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                eventRepository.insertEvent(event)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add event: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun initializeSampleData() {
        viewModelScope.launch {
            try {
                // Only populate if no events exist
                if (events.value.isEmpty()) {
                    eventRepository.populateSampleData()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to initialize sample data: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
