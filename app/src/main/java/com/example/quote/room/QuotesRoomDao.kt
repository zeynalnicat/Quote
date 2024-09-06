package com.example.quote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quote.data.entity.QuoteRoomEntity


@Dao
interface QuotesRoomDao {

    @Query("Select * from quotes")
    suspend fun getAll(): List<QuoteRoomEntity>

    @Insert
    suspend fun insert(quoteRoomEntity: QuoteRoomEntity): Long

    @Query("SELECT id FROM quotes WHERE content = :content LIMIT 1")
    suspend fun search(content: String): Int?

    @Delete
    suspend fun delete(quoteRoomEntity: QuoteRoomEntity)
}