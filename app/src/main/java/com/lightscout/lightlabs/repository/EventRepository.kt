package com.lightscout.lightlabs.repository

import com.lightscout.lightlabs.data.Event
import com.lightscout.lightlabs.data.EventDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {

    fun getAllActiveEvents(): Flow<List<Event>> = eventDao.getAllActiveEvents()

    suspend fun getEventById(id: Long): Event? = eventDao.getEventById(id)

    suspend fun getEventsByCategory(category: String): List<Event> =
            eventDao.getEventsByCategory(category)

    suspend fun insertEvent(event: Event): Long = eventDao.insertEvent(event)

    suspend fun insertEvents(events: List<Event>) = eventDao.insertEvents(events)

    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)

    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)

    suspend fun updateAttendeeCount(eventId: Long, count: Int) =
            eventDao.updateAttendeeCount(eventId, count)

    suspend fun deleteInactiveEvents() = eventDao.deleteInactiveEvents()

    // Sample data for demo purposes
    suspend fun populateSampleData() {
        val sampleEvents =
                listOf(
                        Event(
                                title = "Tech Conference 2024",
                                description =
                                        "Annual technology conference featuring latest innovations",
                                latitude = 37.7749,
                                longitude = -122.4194,
                                address = "San Francisco, CA",
                                startTime = System.currentTimeMillis() + 86400000, // Tomorrow
                                endTime =
                                        System.currentTimeMillis() +
                                                172800000, // Day after tomorrow
                                attendeeCount = 250,
                                category = "Technology"
                        ),
                        Event(
                                title = "Mobile Development Workshop",
                                description = "Hands-on workshop for Android and iOS development",
                                latitude = 37.7849,
                                longitude = -122.4094,
                                address = "Palo Alto, CA",
                                startTime = System.currentTimeMillis() + 259200000, // 3 days
                                endTime =
                                        System.currentTimeMillis() + 266400000, // 3 days + 2 hours
                                attendeeCount = 50,
                                category = "Workshop"
                        ),
                        Event(
                                title = "Startup Networking Event",
                                description = "Connect with entrepreneurs and investors",
                                latitude = 37.7649,
                                longitude = -122.4294,
                                address = "Mountain View, CA",
                                startTime = System.currentTimeMillis() + 432000000, // 5 days
                                endTime =
                                        System.currentTimeMillis() + 439200000, // 5 days + 2 hours
                                attendeeCount = 120,
                                category = "Networking"
                        )
                )

        insertEvents(sampleEvents)
    }
}
