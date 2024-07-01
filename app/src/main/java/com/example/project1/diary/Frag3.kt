package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.contact.ContactHandler
import com.example.project1.databinding.ActivityMainBinding


class Frag3 : Fragment() {
    lateinit var recyclerView : RecyclerView
    private var diaryList = arrayListOf<Diaries>()
    private var addDiary: Button? = null
    lateinit var diariesFragment: Frag3

    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_frag3, container, false)

        binding = ActivityMainBinding.inflate(layoutInflater)

        addDiary = rootView?.findViewById(R.id.add_diaries)
        addDiary?.setOnClickListener{
            val intent = Intent(activity, DiaryAdder::class.java)
            startActivity(intent)
        }

        diariesFragment = this

        val dh = DiaryHandler(context)
        diaryList = dh.getDiariesList()

        val ch = ContactHandler(context)
        val diaryAdapter = DiaryAdapter(diaryList)
        diaryAdapter.setOnItemClickListener(object:
            DiaryAdapter.OnItemClickListener {
            override fun onCardViewClick(view: View, diaries: Diaries, pos: Int) {
                val intent = Intent(activity, DiaryProfile::class.java)
                val name = view.findViewById<TextView>(R.id.diaryName).text.toString()
                val tar = diaryList.find { it.diaryName == name }!!
                val tag = tar.diaryTag
                val memo = tar.diaryMemo
                intent.putStringArrayListExtra("diaryprofile", arrayListOf(name, tag, memo))
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(diariesFragment)
                    ?.commit()
                startActivity(intent)

            }
        }
        )




        recyclerView = rootView.findViewById(R.id.recyclerView_Diary) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = diaryAdapter
        diaryAdapter.notifyDataSetChanged()
        return rootView
    }
}