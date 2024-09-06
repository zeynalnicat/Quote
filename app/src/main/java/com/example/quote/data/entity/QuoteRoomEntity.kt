package com.example.quote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Quotes")
data class QuoteRoomEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val content:String ,
    val author:String,
    val category:String
)