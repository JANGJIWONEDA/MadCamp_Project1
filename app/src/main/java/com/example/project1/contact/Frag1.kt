package com.example.project1.contact

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.MainActivity
import com.example.project1.R
import com.example.project1.databinding.ActivityMainBinding
import com.google.gson.Gson


class Frag1 : Fragment() {
    lateinit var recyclerView : RecyclerView
    private var contactList = arrayListOf<Contacts>()
    private var addContact: Button? = null
    lateinit var contactsFragment: Frag1
    private var addFromLocalButton: Button? = null

    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_frag1, container, false)

        binding = ActivityMainBinding.inflate(layoutInflater)

        addContact = rootView?.findViewById(R.id.add_contacts)
        addContact?.setOnClickListener{
            val intent = Intent(activity, ContactAdder::class.java)
            startActivity(intent)
        }

        contactsFragment = this

        val ch = ContactHandler(context)
        contactList = ch.getContactsList()


        var resolver = requireContext().contentResolver
        requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val cursor = resolver.query(
                    it.data!!.data!!,
                    arrayOf<String>(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ),
                    null,
                    null,
                    null
                )
                Log.d("test", "cursor size: ${cursor?.count}")

                if (cursor!!.moveToFirst()) {
                    val name = cursor.getString(0)
                    val phone = cursor.getString(1)
                    Log.d("test", "name:${name}, phone:${phone}")
                    addToContactListFromLocal(name, phone)
                }
            }
        }

        val contactAdapter = ContactAdapter(contactList)
        contactAdapter.setOnItemClickListener(object:
            ContactAdapter.OnItemClickListener{
            override fun onCardViewClick(view: View, contacts: Contacts, pos: Int) {
                val intent = Intent(activity, ContactProfile::class.java)
                val name = view.findViewById<TextView>(R.id.contactName).text.toString()
                val phone = view.findViewById<TextView>(R.id.phoneNumber).text.toString()
                val relation = view.findViewById<TextView>(R.id.contactRelation).text.toString()
                val contact = contactList.find{it.contactName == name}
                intent.putStringArrayListExtra("contactprofile", arrayListOf(name, phone, relation, contact?.contactTag1, contact?.contactTag2, contact?.contactTag3))
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(contactsFragment)
                    ?.commit()
                startActivity(intent)
                }
            }
        )

        addFromLocalButton = rootView?.findViewById(R.id.add_from_local_point)

        addFromLocalButton?.setOnClickListener{
            onClickFromLocalContactButton(it)
        }



        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = contactAdapter
        contactAdapter.notifyDataSetChanged()
        return rootView
    }
    private fun onClickFromLocalContactButton(view: View){
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            requestLauncher.launch(intent)
    }
    private fun addToContactListFromLocal(newContactName: String, newContactPhone: String){
        val ch = ContactHandler(context)
        val contactList = ch.getContactsList()
        val newContact = Contacts(newContactName, newContactPhone, "", "", "", "")
        contactList.add(newContact)
        val gson = Gson()
        val newContactsListJson: String = gson.toJson(contactList)
        ch.writeContactList(newContactsListJson)
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
}