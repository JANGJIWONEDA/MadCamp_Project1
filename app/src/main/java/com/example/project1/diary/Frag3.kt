package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.project1.MainActivity
import com.example.project1.MainMenu
import com.example.project1.R
import com.example.project1.contact.ContactHandler
import com.example.project1.databinding.ActivityMainBinding

class Frag3 : Fragment() {
    lateinit var recyclerView: RecyclerView
    private var diaryList = arrayListOf<Diaries>()
    private var addDiary: Button? = null
    lateinit var diariesFragment: Frag3

    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_frag3, container, false)

        // Apply animation here
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation3)
        rootView.startAnimation(animation)

        // Initialize views
        recyclerView = rootView.findViewById(R.id.recyclerView_Diary)
        addDiary = rootView.findViewById(R.id.add_diaries)

        // Set up RecyclerView with StaggeredGridLayoutManager
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)

        diariesFragment = this

        val dh = DiaryHandler(context)
        diaryList = dh.getDiariesList()

        val ch = ContactHandler(context)
        val diaryAdapter = DiaryAdapter(diaryList)
        diaryAdapter.setOnItemClickListener(object :
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
        })

        recyclerView.adapter = diaryAdapter
        diaryAdapter.notifyDataSetChanged()

        addDiary?.setOnClickListener {
            onClickAddDiaryButton(it)
        }

        return rootView
    }

    fun onClickAddDiaryButton(view: View?) {
        val intent = Intent(activity, DiaryAdder::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireActivity(), MainMenu::class.java).apply {
                }
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }
}
