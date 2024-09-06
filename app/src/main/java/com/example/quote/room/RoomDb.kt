package com.example.quote.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quote.data.entity.QuoteRoomEntity


@Database(entities = [QuoteRoomEntity::class], version = 1)
abstract class RoomDb:RoomDatabase() {

    abstract fun getDao():QuotesRoomDao

}