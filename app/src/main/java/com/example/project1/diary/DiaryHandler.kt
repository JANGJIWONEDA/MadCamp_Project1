package com.example.project1.diary

import android.content.Context
import org.json.JSONArray
import org.json.JSONTokener
import java.io.File

class DiaryHandler(val context: Context?) {
    fun getDiariesList(): ArrayList<Diaries> {
        val diaryJsonFile = File(context!!.filesDir, "diaries.json")
        val diaryList = ArrayList<Diaries>()


        if(diaryJsonFile.exists()) {
            val diaryJsonString = diaryJsonFile.readText()

            if (diaryJsonString.isNotEmpty()) {
                val diaryJsonArray = JSONTokener(diaryJsonString).nextValue() as JSONArray
                for(i in 0 until diaryJsonArray.length()){
                    val name = diaryJsonArray.getJSONObject(i).getString("diaryName")
                    val tag = diaryJsonArray.getJSONObject(i).getString("diaryTag")
                    val timestamp = System.currentTimeMillis()
                    diaryList.add(Diaries(diaryName = name, diaryTag = tag, timestamp = timestamp))
                }
            }
        }
        return diaryList
    }
//    fun writeDiaryList(newData: String) {
//        val diaryJsonFile = File(context!!.filesDir, "diaries.json")
//        if(!diaryJsonFile.exists()){
//            context.openFileOutput("diaries.json", Context.MODE_PRIVATE)
//        }
//        diaryJsonFile.writeText(newData)
//    }
}