package com.example.project1.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.Memo.databse.NoteDatabase
import com.example.project1.Memo.repository.NoteRepository
import com.example.project1.Memo.viewmodel.NoteViewModel
import com.example.project1.Memo.viewmodel.NoteViewModelFactory
import com.example.project1.R

class Diary_frag3 : Fragment(R.layout.fragment_diary_frag3) {
    lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment1의 XML 레이아웃 파일을 인플레이트하여 반환합니다.
        val rootView = inflater.inflate(R.layout.activity_memo_test, container, false) // XML 파일명 수정

        setupViewModel()

        return rootView
    }


    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(requireContext()))
        val viewModelProviderFactory = NoteViewModelFactory(requireActivity().getApplication(), noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
    companion object {
        fun newInstance(number: Int): Diary_frag3 {
            val fragment1 = Diary_frag3()
            val bundle = Bundle()
            bundle.putInt("number", number)
            fragment1.arguments = bundle
            return fragment1
        }
    }
}
