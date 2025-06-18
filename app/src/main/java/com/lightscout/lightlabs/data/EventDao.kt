package com.lightscout.lightlabs.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events WHERE isActive = 1 ORDER BY startTime ASC")
    fun getAllActiveEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id") suspend fun getEventById(id: Long): Event?

    @Query("SELECT * FROM events WHERE category = :category AND isActive = 1")
    suspend fun getEventsByCategory(category: String): List<Event>

    @Query("UPDATE events SET attendeeCount = :count WHERE id = :eventId")
    suspend fun updateAttendeeCount(eventId: Long, count: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertEvent(event: Event): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertEvents(events: List<Event>)

    @Update suspend fun updateEvent(event: Event)

    @Delete suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM events WHERE isActive = 0") suspend fun deleteInactiveEvents()
}
