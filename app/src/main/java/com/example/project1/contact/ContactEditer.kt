package com.example.project1.contact

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson

class ContactEditer : AppCompatActivity() {
    private lateinit var profileName: EditText
    private lateinit var profilePhone: EditText
    private lateinit var profileRelation: EditText
    private lateinit var profileTag1: EditText
    private lateinit var profileTag2: EditText
    private lateinit var profileTag3: EditText
    private lateinit var editButton: Button
    private var oldName: String? = null
    private var oldPhone: String? = null
    private var oldRelation: String? = null
    private var oldTag1: String? = null
    private var oldTag2: String? = null
    private var oldTag3: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_editer)

        profileName = findViewById(R.id.edit_name)
        profilePhone = findViewById(R.id.edit_phone)
        profileRelation = findViewById(R.id.edit_relation)
        profileTag1 = findViewById(R.id.edit_tag1)
        profileTag2 = findViewById(R.id.edit_tag2)
        profileTag3 = findViewById(R.id.edit_tag3)

        val data = intent.getStringArrayListExtra("contactedit")

        oldName = data?.get(0)
        oldPhone = data?.get(1)
        oldRelation = data?.get(2)
        oldTag1 = data?.get(3)
        oldTag2 = data?.get(4)
        oldTag3 = data?.get(5)
        profileName.hint = oldName
        profilePhone.hint = oldPhone
        profileRelation.hint = oldRelation
        profileTag1.hint = oldTag1
        profileTag2.hint = oldTag2
        profileTag3.hint = oldTag3

        editButton = findViewById(R.id.edited_point)

        editButton.setOnClickListener{
            onClickEditContactButton(it)
        }

    }

    fun onClickEditContactButton(view: View) {
        val ch = ContactHandler(applicationContext)
        val contactList = ch.getContactsList()

        var newContactName = profileName.text.toString()
        var newContactPhone = profilePhone.text.toString()
        var newContactRelation = profileRelation.text.toString()
        var newContactTag1 = profileTag1.text.toString()
        var newContactTag2 = profileTag2.text.toString()
        var newContactTag3 = profileTag3.text.toString()

        if (newContactName == "" && oldName != null) {
            newContactName = oldName as String
        }
        if (newContactPhone == "" && oldPhone != null) {
            newContactPhone = oldPhone as String
        }
        if (newContactRelation == "" && oldRelation != null) {
            newContactRelation = oldRelation as String
        }
        if (newContactTag1 == "" && oldTag1 != null) {
            newContactTag1 = oldTag1 as String
        }
        if (newContactTag2 == "" && oldTag2 != null) {
            newContactTag2 = oldTag2 as String
        }
        if (newContactTag3 == "" && oldTag3 != null) {
            newContactTag3 = oldTag3 as String
        }

        val newContact = Contacts(newContactName, newContactPhone, newContactRelation, newContactTag1, newContactTag2, newContactTag3)
        var filteredContactList = contactList.filter{ con -> con.contactName != oldName}

        filteredContactList = filteredContactList.toMutableList()
        filteredContactList.add(newContact)

        val gson = Gson()
        val newContactsListJson: String = gson.toJson(filteredContactList)
        ch.writeContactList(newContactsListJson)

        val intent = Intent(this@ContactEditer, MainActivity::class.java)
        startActivity(intent)
    }

    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ContactEditer, ContactProfile::class.java)
                startActivity(intent)
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}
