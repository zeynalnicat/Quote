package com.example.quote.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quote.retrofit.QuoteDao
import com.example.quote.room.QuotesRoomDao
import com.example.quote.room.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): RoomDb {
        val room =
            Room.databaseBuilder(context.applicationContext, RoomDb::class.java, "Quotes").build()

        return room

    }

    @Provides
    @Singleton
    fun provideDao(roomDb: RoomDb): QuotesRoomDao = roomDb.getDao()
}