package com.lightscout.lightlabs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.lightscout.lightlabs.data.Event
import com.lightscout.lightlabs.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(viewModel: EventViewModel = hiltViewModel()) {
    val events by viewModel.filteredEvents.collectAsStateWithLifecycle()
    val selectedEvent by viewModel.selectedEvent.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    // Default location (San Francisco Bay Area)
    val defaultLocation = LatLng(37.7749, -122.4194)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = "Event Map",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                )
                Text(
                        text = "Tap events to view details • ${events.size} events",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Category filter
        Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = "Event Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        FilterChip(
                                onClick = { viewModel.selectCategory(category) },
                                label = { Text(category) },
                                selected = category == selectedCategory
                        )
                    }
                }
            }
        }

        // Map
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { viewModel.clearSelectedEvent() }
            ) {
                events.forEach { event ->
                    val position = LatLng(event.latitude, event.longitude)
                    Marker(
                            state = MarkerState(position = position),
                            title = event.title,
                            snippet = "${event.category} • ${event.attendeeCount} attendees",
                            onClick = {
                                viewModel.selectEvent(event)
                                true
                            }
                    )
                }
            }

            // Loading indicator
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Card(
                            colors =
                                    CardDefaults.cardColors(
                                            containerColor =
                                                    MaterialTheme.colorScheme.surface.copy(
                                                            alpha = 0.9f
                                                    )
                                    )
                    ) { Box(modifier = Modifier.padding(24.dp)) { CircularProgressIndicator() } }
                }
            }
        }

        // Selected event details
        selectedEvent?.let { event ->
            EventDetailsCard(
                    event = event,
                    onJoinEvent = { viewModel.incrementAttendeeCount(event.id) },
                    onDismiss = { viewModel.clearSelectedEvent() }
            )
        }

        // Error message
        errorMessage?.let { message ->
            Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                            )
            ) {
                Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { viewModel.clearError() }) { Text("Dismiss") }
                }
            }
        }
    }
}

@Composable
private fun EventDetailsCard(event: Event, onJoinEvent: () -> Unit, onDismiss: () -> Unit) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())

    Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
            ) {
                Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onDismiss) { Text("✕") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "📍 ${event.address}", style = MaterialTheme.typography.bodySmall)
                    Text(
                            text = "📅 ${dateFormat.format(Date(event.startTime))}",
                            style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                            text = "👥 ${event.attendeeCount} attendees",
                            style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "🏷️ ${event.category}", style = MaterialTheme.typography.bodySmall)
                }

                AssistChip(
                        onClick = onJoinEvent,
                        label = { Text("Join Event") },
                        modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}
