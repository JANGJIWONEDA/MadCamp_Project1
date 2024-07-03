package com.example.project1.Memo.fragments

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.example.project1.Memo.model.Note
import com.example.project1.Memo.viewmodel.NoteViewModel
import com.example.project1.R
import com.example.project1.databinding.FragmentEditNoteBinding
import com.example.project1.diary.DiaryProfile

/**
 * A simple [Fragment] subclass.
 * Use the [EditNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditNoteFragment : Fragment(R.layout.fragment_edit_note){

    private var editNoteBinding: FragmentEditNoteBinding?= null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private lateinit var tag: String

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        tag = (activity as DiaryProfile).diary_id
        notesViewModel = (activity as DiaryProfile).noteViewModel
        currentNote = args.note!!

        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)

        binding.editNoteFab.setOnClickListener{
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if (noteTitle.isNotEmpty()){
                    val note = Note(currentNote.id, noteTitle, noteDesc, tag)
                    notesViewModel.updateNote(note)
                    view.findNavController().popBackStack(R.id.homeFragment, false)
                } else{
                    Toast.makeText(context, "Please enter note title", Toast.LENGTH_SHORT).show()
            }
        }
        binding.deleteNoteButton.setOnClickListener{
            deleteNote()
        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(activity).apply{
            setTitle("Delete Note")
            setMessage("Do you want to delete this note?")
            setPositiveButton("Delete"){_, _ ->
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onDestroy(){
            super.onDestroy()
            editNoteBinding = null
    }

    public final fun deleteNoteAll(notesViewModel: NoteViewModel, tag:String){
        notesViewModel.deleteNoteById(tag)
    }
}