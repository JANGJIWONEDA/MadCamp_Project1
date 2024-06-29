package com.example.project1.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.contact.ContactAdapter
import com.example.project1.contact.ContactHandler

class Diary_frag1 : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileTag: TextView
    lateinit var recyclerView : RecyclerView


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

        val ch = ContactHandler(context)
        val contactList = ch.getContactsList()
        val filteredContactList = contactList.filter{it -> it.contactTag1 == tag || it.contactTag2 == tag || it.contactTag3 == tag}

        val contactAdapter = ContactAdapter(filteredContactList)

        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = contactAdapter


        return rootView
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
