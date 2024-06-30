package com.example.project1.diary

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.MainActivity
import com.example.project1.R
import com.example.project1.contact.ContactAdapter
import com.example.project1.contact.ContactEditer
import com.example.project1.contact.ContactHandler
import com.example.project1.contact.ContactProfile
import com.example.project1.contact.Contacts
import com.google.gson.Gson

class Diary_frag1 : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileTag: TextView
    lateinit var recyclerView : RecyclerView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var diaries_frag: Diary_frag1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment1의 XML 레이아웃 파일을 인플레이트하여 반환합니다.

        val rootView =  inflater.inflate(R.layout.fragment_diary_frag1, container, false) // XML 파일명 수정
        val name = arguments?.getString("name") ?: ""
        val tag = arguments?.getString("tag") ?: ""
        profileName = rootView.findViewById(R.id.travelDiaryName)
        profileTag = rootView.findViewById(R.id.travelDiaryTag)

        profileName.text = name
        profileTag.text = tag

        diaries_frag = this

        val ch = ContactHandler(context)
        val contactList = ch.getContactsList()
        val filteredContactList = contactList.filter{it -> it.contactTag1 == tag || it.contactTag2 == tag || it.contactTag3 == tag}

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
            onClickDeleteDiaryButton(it)

        }


        return rootView
    }

    private fun onClickDeleteDiaryButton(view: View){
        val dh = DiaryHandler(context)
        val diaryList = dh.getDiariesList()

        Log.d("test", "before getting found")
        val diaryName = profileName.text.toString()
        Log.d("test", "after getting $diaryName")
        val filteredDiaryList = diaryList.filter{ di -> di.diaryName != diaryName}
        Log.d("test", "$diaryName found")
        val gson = Gson()
        val newDiaryListJson: String = gson.toJson(filteredDiaryList)

        dh.writeDiaryList(newDiaryListJson)

        Toast.makeText(context, "$diaryName 를 잊어버렸어요", Toast.LENGTH_SHORT).show()

        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun onClickEditDiaryButton(view: View){
        val intent = Intent(activity, DiaryEditer::class.java)
        intent.putStringArrayListExtra("diaryedit", arrayListOf(
            profileName.text.toString(),
            profileTag.text.toString()))

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
