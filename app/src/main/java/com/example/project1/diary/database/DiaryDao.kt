package com.example.project1.diary.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.project1.diary.Diaries

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: Diaries): Long

    @Update
    suspend fun updateDiary(diary: Diaries): Int

    @Delete
    suspend fun deleteDiary(diary: Diaries)

    @Query("SELECT * FROM diaries ORDER BY timestamp DESC")
    fun getAllDiaries(): LiveData<List<Diaries>>

    @Query("SELECT * FROM diaries WHERE id = :id")
    fun getDiaryById(id: Int): LiveData<Diaries>

}
