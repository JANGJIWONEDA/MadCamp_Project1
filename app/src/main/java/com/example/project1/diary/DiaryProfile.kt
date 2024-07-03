package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.project1.MainActivity
import com.example.project1.Memo.databse.NoteDatabase
import com.example.project1.Memo.repository.NoteRepository
import com.example.project1.Memo.viewmodel.NoteViewModel
import com.example.project1.Memo.viewmodel.NoteViewModelFactory
import com.example.project1.R

class DiaryProfile : AppCompatActivity() {
    private var viewPager2: ViewPager2? = null
    lateinit var noteViewModel: NoteViewModel
    lateinit var tag_d: String
    lateinit var name:  String
    lateinit var diary_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_profile) // XML 파일명 수정

        val data = intent.getStringArrayListExtra("diaryprofile")
        name = data?.get(0) ?: ""
        tag_d = data?.get(1)?:""
        diary_id = data?.get(2)?:""
        val fragments = ArrayList<Fragment>()
        Log.d("test in DIARYPROFILE", "Name: ${name}, Tag:${tag_d}")
        fragments.add(Diary_frag1.newInstance(0).apply{
            arguments = Bundle().apply{
                putString("name", name)
                putString("id", diary_id)
                putString("tag", tag_d)
            }
        })
        fragments.add(Diary_frag2.newInstance(1).apply{
            arguments = Bundle().apply{
                putString("tag", tag_d)
            }
        })
        fragments.add(Diary_frag3.newInstance(2).apply{
            arguments = Bundle().apply{
                putString("tag", tag)
            }
        })

        setupViewModel()
        viewPager2 = findViewById<ViewPager2>(R.id.viewPager2_container)
        val viewPager2Adapter = Diary_flag_adapter(this, fragments)
        viewPager2!!.adapter = viewPager2Adapter
    }
    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Start MainActivity and navigate to Frag3
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fragment", "frag3")
        startActivity(intent)
        finish() // Finish the current activity
    }
}