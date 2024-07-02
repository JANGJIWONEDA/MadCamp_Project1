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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.project1.MainActivity
import com.example.project1.Memo.model.Note
import com.example.project1.R
import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.repository.DiaryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DiaryEditer : AppCompatActivity() {
    private lateinit var profileName: EditText
    private lateinit var profileTag: EditText
    private lateinit var editButton: Button
    private var oldName: String? = null
    private var oldTag: String? = null
    private final var oldId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_editer)

        profileName = findViewById(R.id.edit_traveltitle)
        profileTag = findViewById(R.id.edit_traveltag)

        val data = intent.getStringArrayListExtra("diaryedit")
        Log.d("test", "intent got")
        oldName = data?.get(0)
        oldTag = data?.get(1)
        oldId = data?.get(2)!!.toInt()
        profileName.hint = oldName
        profileTag.hint = oldTag

        editButton = findViewById(R.id.edited_button)

        editButton.setOnClickListener{
            onClickEditDiaryButton(it)
        }
    }

    fun onClickEditDiaryButton(view: View) {
        val dh = DiaryHandler(applicationContext)
//        val diaryList = dh.getDiariesList()

        var newDiaryName = profileName.text.toString()
        var newDiaryTag = profileTag.text.toString()

        if (newDiaryName == "" && oldName != null) {
            newDiaryName = oldName as String
        }

        if (newDiaryTag == "" && oldTag != null) {
            newDiaryTag = oldTag as String
        }


        val timestamp = System.currentTimeMillis()

        val diaryRepository = DiaryRepository(DiaryDatabase(this))
        Log.d("test", "${newDiaryTag}, ${oldId}")
        val newDiary = Diaries(id = oldId, diaryName = newDiaryName, diaryTag = newDiaryTag, timestamp = timestamp)
        lifecycleScope.launch{
            diaryRepository.updateDiary(newDiary)
            Toast.makeText(applicationContext, "수정되었어요", Toast.LENGTH_SHORT).show()
        }


        val intent = Intent(this@DiaryEditer, MainActivity::class.java)
        intent.putExtra("fragment", "frag3")
        startActivity(intent)

        intent.putStringArrayListExtra("diaryprofile", arrayListOf(newDiaryName, newDiaryTag, oldId.toString()))
        startActivity(intent)
        finish()
    }

    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DiaryEditer, MainActivity::class.java)
                startActivity(intent)
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}
