package com.example.project1.diary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.project1.R

class DiaryProfile : AppCompatActivity() {
    private var viewPager2: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_profile) // XML 파일명 수정

        val fragments = ArrayList<Fragment>()
        fragments.add(Diary_frag1.newInstance(0))
        fragments.add(Diary_frag2.newInstance(1))

        viewPager2 = findViewById<ViewPager2>(R.id.viewPager2_container)
        val viewPager2Adapter = Diary_flag_adapter(this, fragments)
        viewPager2!!.adapter = viewPager2Adapter
    }
}