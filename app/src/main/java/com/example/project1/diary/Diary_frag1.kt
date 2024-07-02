package com.example.project1.diary

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.MainActivity
import com.example.project1.R
import com.example.project1.contact.ContactAdapter
import com.example.project1.contact.ContactEditer
import com.example.project1.contact.ContactHandler
import com.example.project1.contact.ContactProfile
import com.example.project1.contact.Contacts
import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.repository.DiaryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.example.project1.Memo.fragments.EditNoteFragment

class Diary_frag1 : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileTag: TextView
    lateinit var recyclerView : RecyclerView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var diaries_frag: Diary_frag1
    private lateinit var diaryRepository: DiaryRepository
    private var id : String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment1의 XML 레이아웃 파일을 인플레이트하여 반환합니다.

        val rootView =  inflater.inflate(R.layout.fragment_diary_frag1, container, false) // XML 파일명 수정
        val name = arguments?.getString("name") ?: ""
        val tag_d = arguments?.getString("tag")
        id = arguments?.getString("id")?:""

        Log.d("test", "tag : ${tag_d} name: ${name}, id:${id}")
        profileName = rootView.findViewById(R.id.travelDiaryName)
        profileTag = rootView.findViewById(R.id.travelDiaryTag)
        profileName.text = name
        profileTag.text = tag_d
        Log.d("test", "${tag_d}, ${profileTag.text}, ${name}, ${profileName.text}, id:${id}")
        diaries_frag = this

        diaryRepository = DiaryRepository(DiaryDatabase(requireContext()))

        val ch = ContactHandler(context)
        val contactList = ch.getContactsList()
        val filteredContactList = contactList.filter{it -> (it.contactTag1 == tag_d || it.contactTag2 == tag_d || it.contactTag3 == tag_d)}

        val contactAdapter = ContactAdapter(filteredContactList)

        contactAdapter.setOnItemClickListener(object:
            ContactAdapter.OnItemClickListener{
            override fun onCardViewClick(view: View, contacts: Contacts, pos: Int) {}
            override fun onCallClick(view:View, contacts: Contacts, pos : Int){
                val status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.CALL_PHONE")
                if (status == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test", "permission granted")
                    var intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:"+view.findViewById<TextView>(R.id.phoneNumber).text.toString())
                    startActivity(intent)
                } else{
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>("android.permission.CALL_PHONE"), 100)
                    Log.d("test", "permission denied")
                }
            }
        }
        )
        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = contactAdapter

        editButton = rootView.findViewById(R.id.edited_point)
        editButton.setOnClickListener{
            onClickEditDiaryButton(it)
        }


        deleteButton = rootView.findViewById(R.id.deleted_point)
        deleteButton.setOnClickListener{
            onClickDeleteDiaryButton(it, id!!.toInt(), name, tag_d!!)

        }


        return rootView
    }

    private fun onClickDeleteDiaryButton(view: View, id : Int, name: String, tag: String){

        viewLifecycleOwner.lifecycleScope.launch {
            diaryRepository.deleteDiary(
                Diaries(
                    id = id,
                    diaryName = name,
                    diaryTag = tag,
                    timestamp = System.currentTimeMillis()
                )
            )
            val viewmodel = (activity as DiaryProfile).noteViewModel
            viewmodel.deleteNoteById(id.toString())
            Toast.makeText(context, "${name}의 기억을 잊어버렸어요", Toast.LENGTH_SHORT).show()
        }

//        val filteredDiaryList = diaryList.filter{ di -> di.diaryName != diaryName}
//        val gson = Gson()
//        val newDiaryListJson: String = gson.toJson(filteredDiaryList)
//
//        dh.writeDiaryList(newDiaryListJson)

        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun onClickEditDiaryButton(view: View){
        val intent = Intent(activity, DiaryEditer::class.java)
        intent.putStringArrayListExtra("diaryedit", arrayListOf(
            profileName.text.toString(),
            profileTag.text.toString(),
            id)
        )

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(diaries_frag)
            ?.commit()
        startActivity(intent)
    }

    companion object {
        fun newInstance(number: Int): Diary_frag1 {
            val fragment1 = Diary_frag1()
            val bundle = Bundle()
            bundle.putInt("number", number)
            fragment1.arguments = bundle
            return fragment1
        }
    }
}
