package com.example.project1.diary.repository

import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.Diaries

class DiaryRepository(private val db: DiaryDatabase) {

    suspend fun insertDiary(diary: Diaries) = db.getDiaryDao().insertDiary(diary)
    suspend fun updateDiary(diary: Diaries):Int = db.getDiaryDao().updateDiary(diary)
    suspend fun deleteDiary(diary: Diaries) = db.getDiaryDao().deleteDiary(diary)

    fun getAllDiaries() = db.getDiaryDao().getAllDiaries()
    fun getDiaryById(id: Int) = db.getDiaryDao().getDiaryById(id)
}