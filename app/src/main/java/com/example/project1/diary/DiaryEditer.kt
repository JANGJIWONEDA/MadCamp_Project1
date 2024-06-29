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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson

class DiaryEditer : AppCompatActivity() {
    private lateinit var profileName: EditText
    private lateinit var profileTag: EditText
    private lateinit var editButton: Button
    private var oldName: String? = null
    private var oldTag: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "Editer enter")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_editer)

        profileName = findViewById(R.id.edit_traveltitle)
        profileTag = findViewById(R.id.edit_traveltag)

        val data = intent.getStringArrayListExtra("diaryedit")
        Log.d("test", "intent got")
        oldName = data?.get(0)
        oldTag = data?.get(1)
        profileName.hint = oldName
        profileTag.hint = oldTag

        editButton = findViewById(R.id.edited_button)

        editButton.setOnClickListener{
            onClickEditDiaryButton(it)
        }

    }

    fun onClickEditDiaryButton(view: View) {
        val dh = DiaryHandler(applicationContext)
        val diaryList = dh.getDiariesList()

        var newDiaryName = profileName.text.toString()
        var newDiaryTag = profileTag.text.toString()

        if (newDiaryName == "" && oldName != null) {
            newDiaryName = oldName as String
        }

        if (newDiaryTag == "" && oldTag != null) {
            newDiaryTag = oldTag as String
        }

        val newDiary = Diaries(newDiaryName, newDiaryTag)
        var filteredDiaryList = diaryList.filter{ di -> di.diaryName != oldName}

        filteredDiaryList = filteredDiaryList.toMutableList()
        filteredDiaryList.add(newDiary)

        val gson = Gson()
        val newDiariesListJson: String = gson.toJson(filteredDiaryList)
        dh.writeDiaryList(newDiariesListJson)

        val intent = Intent(this@DiaryEditer, Frag3::class.java)
        startActivity(intent)
    }

    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DiaryEditer, DiaryProfile::class.java)
                startActivity(intent)
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}
