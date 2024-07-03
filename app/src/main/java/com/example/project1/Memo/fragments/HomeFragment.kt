package com.example.project1.Memo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.project1.Memo.MemoTest
import com.example.project1.Memo.adapter.NoteAdapter
import com.example.project1.Memo.model.Note
import com.example.project1.Memo.viewmodel.NoteViewModel
import com.example.project1.R
import com.example.project1.databinding.FragmentHomeBinding
import com.example.project1.diary.DiaryProfile
import androidx.lifecycle.map
import androidx.lifecycle.switchMap

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {
    // TODO: Rename and change types of parameters
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var tag : String

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var notesViewModel : NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        tag = (activity as DiaryProfile).diary_id

        notesViewModel = (activity as DiaryProfile).noteViewModel
        setupHomeRecyclerView()


        binding.addNoteFab.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
        binding.memoSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    val allNotesLiveData = notesViewModel.getAllNotes()
                    allNotesLiveData.observe(viewLifecycleOwner) { allNotes ->
                        val filteredNotes = allNotes.filter { note ->
                            note.noteTag == tag
                        }
                        noteAdapter.differ.submitList(filteredNotes)
                        updateUI(filteredNotes)
                    }
                }
                else{
                    Log.d("test in HOMEFRA`GMENT", "${newText}")
                    searchNote(newText)
                }
                return true
            }
        })
    }

    private fun updateUI(note: List<Note>?){
        if (note != null){
            if(note.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView(){
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply{
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL )
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {

            val _allNotes = notesViewModel.getAllNotes()
            val _filterQuery = MutableLiveData<String>()

            val filteredNotes: LiveData<List<Note>> = _filterQuery.switchMap { query ->
                _allNotes.map { notes ->
                    notes.filter { note ->
                        note.noteTag == tag
                    }
                }
            }
            _allNotes.observe(viewLifecycleOwner){notes ->
                Log.d("Test Number of items", "Num of notes: ${notes.size}")
            }
            _filterQuery.value = ""
            filteredNotes.observe(viewLifecycleOwner) { notes ->
                Log.d("HomeFragment", "Filtered notes: ${notes.size}")
                noteAdapter.differ.submitList(notes)
                updateUI(notes)
            }
        }

    }
    private fun searchNote(query: String?){
        val searchQuery = "%$query%" ?: ""
        Log.d("Test searchNote", "${searchQuery}")
        notesViewModel.searchNote(searchQuery).observe(this, Observer { searchResults ->
            val filteredResultsLiveData = MutableLiveData<List<Note>>()
            filteredResultsLiveData.value = searchResults.filter { note ->
                note.noteTag == tag
            }
//            noteAdapter.differ.submitList(filteredResultsLiveData)})
            filteredResultsLiveData.observe(viewLifecycleOwner, Observer { filteredResults ->
                noteAdapter.differ.submitList(filteredResults)
                updateUI(filteredResults)
            })
        })
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()) {
            binding.memoSearchView.visibility = View.GONE
        }
        else{
            Log.d("test in HOMEFRA`GMENT", "${newText}")
            binding.memoSearchView.visibility = View.VISIBLE
            searchNote(newText)
        }
        return true
    }
    override fun onDestroy(){
        super .onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
    fun setViewModel(viewModel: NoteViewModel) {
        notesViewModel = viewModel
    }
}