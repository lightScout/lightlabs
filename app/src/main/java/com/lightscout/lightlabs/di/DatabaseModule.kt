package com.lightscout.lightlabs.di

import android.content.Context
import androidx.room.Room
import com.lightscout.lightlabs.data.EventDao
import com.lightscout.lightlabs.data.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEventDatabase(@ApplicationContext context: Context): EventDatabase {
        return Room.databaseBuilder(context, EventDatabase::class.java, EventDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEventDao(database: EventDatabase): EventDao {
        return database.eventDao()
    }
}
