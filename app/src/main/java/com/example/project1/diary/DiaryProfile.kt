package com.example.project1.diary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.project1.Memo.databse.NoteDatabase
import com.example.project1.Memo.fragments.HomeFragment
import com.example.project1.Memo.repository.NoteRepository
import com.example.project1.Memo.viewmodel.NoteViewModel
import com.example.project1.Memo.viewmodel.NoteViewModelFactory
import com.example.project1.R

class DiaryProfile : AppCompatActivity() {
    private var viewPager2: ViewPager2? = null
    lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_profile) // XML 파일명 수정

        val data = intent.getStringArrayListExtra("diaryprofile")
        val name = data?.get(0) ?: ""
        val tag = data?.get(1)?:""
        val memo = data?.get(2)?:""
        val fragments = ArrayList<Fragment>()
        fragments.add(Diary_frag1.newInstance(0).apply{
            arguments = Bundle().apply{
                putString("name", name)
                putString("tag", tag)
            }
        })
        fragments.add(Diary_frag2.newInstance(1).apply{
            arguments = Bundle().apply{
                putString("tag", tag)
            }
        })

        fragments.add(Diary_frag3.newInstance(2).apply{
            arguments = Bundle().apply{
                putString("memo", memo)
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
}