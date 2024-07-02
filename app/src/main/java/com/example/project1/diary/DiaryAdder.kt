package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson

class DiaryAdder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_adder)
    }

    fun onClickAddDiaryButton(view: View){
        val dh = DiaryHandler(applicationContext)
        val diaryList = dh.getDiariesList()

        var newDiaryTitle = findViewById<EditText>(R.id.add_TravelTitle).text.toString()
        var newDiaryTag = findViewById<EditText>(R.id.add_TravelTag).text.toString()
        for(diary in diaryList){
            if(diary.diaryName == newDiaryTitle){
                Toast.makeText(applicationContext, "이 추억은 이미 저장되어 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val newDiary = Diaries(newDiaryTitle, newDiaryTag, "")
        diaryList.add(newDiary)


        val gson = Gson()
        val newDiariesListJson: String = gson.toJson(diaryList)
        dh.writeDiaryList(newDiariesListJson)

        val intent = Intent(this@DiaryAdder, MainActivity::class.java)
        intent.putExtra("fragment", "frag3")
        startActivity(intent)
    }
//    private fun addOnBackPressedCallback() {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val intent = Intent(this@ContactAdder, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        this.onBackPressedDispatcher.addCallback(this, callback)
//    }
}