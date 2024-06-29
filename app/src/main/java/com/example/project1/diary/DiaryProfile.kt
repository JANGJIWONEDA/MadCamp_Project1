package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson
import org.w3c.dom.Text

class DiaryProfile : AppCompatActivity() {
    private lateinit var profileName: TextView
    private lateinit var profileTag: TextView
    private lateinit var finishButton : Button
    private lateinit var editButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_profile)

        val data = intent.getStringArrayListExtra("diaryprofile")
        profileName = findViewById(R.id.travelDiaryName)
        profileTag = findViewById(R.id.travelDiaryTag)




        profileName.text = data?.get(0)
        profileTag.text = data?.get(1)

        finishButton = findViewById(R.id.end_point)

        finishButton.setOnClickListener{
            onClickDeleteDiaryButton(it)
        }

        editButton = findViewById(R.id.edit_point)

        editButton.setOnClickListener{
            onClickEditDiaryButton(it)
        }
    }
    private fun onClickDeleteDiaryButton(view: View){
        val ch = DiaryHandler(applicationContext)
        val diaryList = ch.getDiariesList()

        val diaryName = findViewById<TextView>(R.id.travelDiaryName).text.toString()
        val filteredDiaryList = diaryList.filter{ con -> con.diaryName != diaryName}

        val gson = Gson()
        val newDiaryListJson: String = gson.toJson(filteredDiaryList)

        ch.writeDiaryList(newDiaryListJson)

        Toast.makeText(applicationContext, "$diaryName 삭제했습니다.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@DiaryProfile, Frag3::class.java)
        startActivity(intent)
    }
    private fun onClickEditDiaryButton(view: View){
        Log.d("test", "EDIT ACCEPTED")
        val diariesFragment = this
        val intent = Intent(this@DiaryProfile, DiaryEditer::class.java)
        intent.putStringArrayListExtra("diaryedit", arrayListOf(
            profileName.text.toString(),
            profileTag.text.toString())
        )
        startActivity(intent)
    }

//    private fun addOnBackPressedCallback() {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                val intent = Intent(this@DiaryProfile, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        this.onBackPressedDispatcher.addCallback(this, callback)
//    }
}