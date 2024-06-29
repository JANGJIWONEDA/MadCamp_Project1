package com.example.project1.diary

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.MainActivity
import com.example.project1.R
import com.example.project1.diary.DiaryAdapter
import com.example.project1.diary.DiaryAdder
import com.example.project1.diary.DiaryHandler
import com.example.project1.diary.DiaryProfile
import com.example.project1.diary.Diaries
import com.example.project1.databinding.ActivityMainBinding
import com.google.gson.Gson


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


        val diaryAdapter = DiaryAdapter(diaryList)
        diaryAdapter.setOnItemClickListener(object:
            DiaryAdapter.OnItemClickListener {
            override fun onCardViewClick(view: View, diaires: Diaries, pos: Int) {
                val intent = Intent(activity, DiaryProfile::class.java)
                val name = view.findViewById<TextView>(R.id.diaryName).text.toString()
                val tag = diaryList.find { it.diaryName == name }?.diaryTag
                intent.putStringArrayListExtra("diaryprofile", arrayListOf(name, tag))
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