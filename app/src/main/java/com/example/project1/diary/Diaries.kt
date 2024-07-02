package com.example.project1.diary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaries")
class Diaries (
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val diaryName: String,
    val diaryTag: String,
    val timestamp: Long)