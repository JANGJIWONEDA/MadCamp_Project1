package com.example.project1.Memo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.project1.Memo.model.Note
import com.example.project1.Memo.viewmodel.NoteViewModel
import com.example.project1.R
import com.example.project1.databinding.FragmentAddNoteBinding
import com.example.project1.diary.DiaryProfile

/**
 * A simple [Fragment] subclass.
 * Use the [AddNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNoteFragment : Fragment(R.layout.fragment_add_note){

    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    private lateinit var tag:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        notesViewModel = (activity as DiaryProfile).noteViewModel
        addNoteView = view

        binding.saveNoteButton.setOnClickListener{
            saveNote(it)
        }
    }

    private fun saveNote(view: View){
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        tag = (activity as DiaryProfile).diary_id
        if(noteTitle.isNotEmpty())
        {
            val note = Note(0, noteTitle, noteDesc, tag)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "NOTE SAVED notestag:${note.noteTag}", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else{
            Toast.makeText(addNoteView.context, "Please enter note title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        addNoteBinding = null
    }

}