package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.project1.MainMenu
import com.example.project1.R
import com.example.project1.databinding.FragmentFrag3Binding
import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.repository.DiaryRepository

class Frag3 : Fragment() {
    lateinit var recyclerView: RecyclerView
    private var addDiary: Button? = null
    lateinit var diariesFragment: Frag3
    lateinit var diaryRepository : DiaryRepository
    lateinit var diaryAdapter: DiaryAdapter
    private var homeBinding: FragmentFrag3Binding? = null
    private val binding get() = homeBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeBinding = FragmentFrag3Binding.inflate(inflater, container, false)
        val rootView = binding.root
        
        // Apply animation here
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation3)
        rootView.startAnimation(animation)

        // Initialize views
        recyclerView = rootView.findViewById(R.id.recyclerView_Diary)
        addDiary = rootView.findViewById(R.id.add_diaries)

        // Set up RecyclerView with StaggeredGridLayoutManager
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)

        diaryRepository = DiaryRepository(DiaryDatabase(requireContext()))
        
        addDiary = rootView.findViewById(R.id.add_diaries)
        addDiary?.setOnClickListener{
            val intent = Intent(activity, DiaryAdder::class.java)
            startActivity(intent)
        }

        diariesFragment = this

        setupHomeRecyclerView()

        recyclerView.adapter = diaryAdapter
        diaryAdapter.notifyDataSetChanged()
        return rootView
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

    private fun updateUI(diary: List<Diaries>?){
        if (diary != null){
            if(diary.isNotEmpty()){
                recyclerView.visibility = View.VISIBLE
            } else{
                recyclerView.visibility = View.GONE
            }
        }
    }
    private fun setupHomeRecyclerView(){
        diaryAdapter = DiaryAdapter()
        recyclerView.adapter = diaryAdapter

        diaryAdapter.setOnItemClickListener(object : DiaryAdapter.OnItemClickListener {
            override fun onCardViewClick(view: View, diaries: Diaries, pos: Int) {
                val intent = Intent(activity, DiaryProfile::class.java)
                Log.d("test : in onCardViewClick", "name: ${diaries.diaryName}, Tag:${diaries.diaryTag}")
                intent.putStringArrayListExtra("diaryprofile", arrayListOf(diaries.diaryName, diaries.diaryTag, diaries.id.toString()))
                startActivity(intent)
            }
        })
        diaryRepository.getAllDiaries().observe(viewLifecycleOwner) { diaries ->
            diaryAdapter.differ.submitList(diaries)
            updateUI(diaries)
        }
    }
}