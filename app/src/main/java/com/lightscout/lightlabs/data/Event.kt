package com.lightscout.lightlabs.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "events")
@Parcelize
data class Event(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val title: String,
        val description: String,
        val latitude: Double,
        val longitude: Double,
        val address: String,
        val startTime: Long,
        val endTime: Long,
        val isActive: Boolean = true,
        val attendeeCount: Int = 0,
        val category: String = "General"
) : Parcelable
