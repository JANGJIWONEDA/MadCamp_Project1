package com.example.project1.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.project1.MainActivity
import com.example.project1.MainMenu
import com.example.project1.Memo.adapter.NoteAdapter
import com.example.project1.Memo.model.Note
import com.example.project1.R
import com.example.project1.contact.ContactHandler
import com.example.project1.databinding.ActivityMainBinding
import com.example.project1.databinding.FragmentFrag3Binding
import com.example.project1.databinding.FragmentHomeBinding
import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.repository.DiaryRepository

class Frag3 : Fragment() {
    lateinit var recyclerView: RecyclerView
    private var diaryList = arrayListOf<Diaries>()
    private var addDiary: Button? = null
    lateinit var diariesFragment: Frag3
    lateinit var diaryRepository : DiaryRepository
    lateinit var diaryAdapter: DiaryAdapter
    lateinit var requestLauncher: ActivityResultLauncher<Intent>
//    lateinit var binding: ActivityMainBinding
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

//        val rootView = inflater.inflate(R.layout.fragment_frag3, container, false)
        diaryRepository = DiaryRepository(DiaryDatabase(requireContext()))
        
        addDiary = rootView.findViewById(R.id.add_diaries)
        addDiary?.setOnClickListener{
            val intent = Intent(activity, DiaryAdder::class.java)
            startActivity(intent)
        }

        diariesFragment = this

        recyclerView = binding.recyclerViewDiary
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dh = DiaryHandler(context)
        diaryList = dh.getDiariesList()
        setupHomeRecyclerView()
        val ch = ContactHandler(context)
//        val diaryAdapter = DiaryAdapter()
//        diaryAdapter.setOnItemClickListener(object:
//            DiaryAdapter.OnItemClickListener {
//            override fun onCardViewClick(view: View, diaries: Diaries, pos: Int) {
//                val intent = Intent(activity, DiaryProfile::class.java)
//                val name = view.findViewById<TextView>(R.id.diaryName).text.toString()
//                val tar = diaryList.find { it.diaryName == name }!!
//                val id = diaries.id.toString()
//                intent.putStringArrayListExtra("diaryprofile", arrayListOf(name, tag, id))
//                activity?.supportFragmentManager
//                    ?.beginTransaction()
//                    ?.remove(diariesFragment)
//                    ?.commit()
//                startActivity(intent)
//
//            }
//        }
//        )




        recyclerView = rootView.findViewById(R.id.recyclerView_Diary) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = diaryAdapter
        diaryAdapter.notifyDataSetChanged()
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