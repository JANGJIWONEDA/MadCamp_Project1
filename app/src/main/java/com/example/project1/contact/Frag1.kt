package com.example.project1.contact

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.MainActivity
import com.example.project1.MainMenu
import com.example.project1.R
import com.example.project1.databinding.ActivityMainBinding
import com.google.gson.Gson

class Frag1 : Fragment() {
    lateinit var recyclerView: RecyclerView
    private var contactList = arrayListOf<Contacts>()
    private var addContact: Button? = null
    lateinit var contactsFragment: Frag1
    private var addFromLocalButton: Button? = null

    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding
    lateinit var contactAdapter: ContactAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_frag1, container, false)

        // Apply animation here
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation3)
        rootView.startAnimation(animation)

        binding = ActivityMainBinding.inflate(layoutInflater)

        addContact = rootView.findViewById(R.id.add_contacts)
        addContact?.setOnClickListener{
            val intent = Intent(activity, ContactAdder::class.java)
            startActivity(intent)
        }

        contactsFragment = this

        val ch = ContactHandler(context)
        contactList = ch.getContactsList()

        var resolver = requireContext().contentResolver
        requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
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

        // SearchView 설정
        val searchView = rootView.findViewById<SearchView>(R.id.contact_search_view)
        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        val customFont: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.font1)
        searchText.typeface = customFont

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContact(newText)
                return true
            }
        })

        contactAdapter = ContactAdapter(contactList)
        contactAdapter.updateContactList(contactList)
        contactAdapter.setOnItemClickListener(object :
            ContactAdapter.OnItemClickListener {
            override fun onCardViewClick(view: View, contacts: Contacts, pos: Int) {
                val intent = Intent(activity, ContactProfile::class.java)
                val name = view.findViewById<TextView>(R.id.contactName).text.toString()
                val phone = view.findViewById<TextView>(R.id.phoneNumber).text.toString()
                val relation = view.findViewById<TextView>(R.id.contactRelation).text.toString()
                val contact = contactList.find { it.contactName == name }
                intent.putStringArrayListExtra("contactprofile", arrayListOf(name, phone, relation, contact?.contactTag1, contact?.contactTag2, contact?.contactTag3))
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(contactsFragment)
                    ?.commit()
                startActivity(intent)
            }

            override fun onCallClick(view: View, contacts: Contacts, pos: Int) {
                val status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.CALL_PHONE")
                if (status == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test", "permission granted")

                    var intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + view.findViewById<TextView>(R.id.phoneNumber).text.toString())
                    startActivity(intent)
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>("android.permission.CALL_PHONE"), 100)
                    Log.d("test", "permission denied")
                }
            }
        })

        addFromLocalButton = rootView.findViewById(R.id.add_from_local_point)

        addFromLocalButton?.setOnClickListener {
            onClickFromLocalContactButton(it)
        }

        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = contactAdapter
        contactAdapter.notifyDataSetChanged()
        return rootView
    }

    private fun onClickFromLocalContactButton(view: View) {
        var status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.READ_CONTACTS")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
            getFromLocalContactButton(view)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>("android.permission.READ_CONTACTS"), 100)
            Log.d("test", "permission denied in contact")
            status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.READ_CONTACTS")
            if (status == PackageManager.PERMISSION_GRANTED) {
                getFromLocalContactButton(view)
            }
        }
    }

    private fun getFromLocalContactButton(view: View) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        requestLauncher.launch(intent)
    }

    private fun addToContactListFromLocal(newContactName: String, newContactPhone: String) {
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

    private fun filterContact(query: String?) {
        val filteredContactList = if (query.isNullOrBlank()) {
            contactList.sortedBy { it.contactName }
        } else {
            contactList.filter { contact ->
                contact.contactName.contains(query, ignoreCase = true)
            }.sortedBy { it.contactName }
        }
        contactAdapter.updateContactList(filteredContactList)
    }

}
