package com.example.project1.contact

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson

class ContactAdder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_adder)
    }

    fun onClickAddContactButton(view: View){
        val ch = ContactHandler(applicationContext)
        val contactList = ch.getContactsList()

        var newContactPhone = findViewById<EditText>(R.id.add_phone).text.toString()
        var newContactName = findViewById<EditText>(R.id.add_name).text.toString()
        val newContactRelation = findViewById<EditText>(R.id.add_relation).text.toString()
        if (newContactPhone == ""){
            newContactPhone = "00000000000"
        }
        if (newContactName == ""){
            newContactName = "noName"
        }
        for(contact in contactList){
            if(contact.contactName == newContactName){
                Toast.makeText(applicationContext, "이 사람은 이미 저장되어 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val newContact = Contacts(newContactName, newContactPhone, newContactRelation)
        contactList.add(newContact)

        val gson = Gson()
        val newContactsListJson: String = gson.toJson(contactList)
        ch.writeContactList(newContactsListJson)

        val intent = Intent(this@ContactAdder, MainActivity::class.java)
        startActivity(intent)
    }
//    private fun addOnBackPressedCallback() {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val intent = Intent(this@ContactAdder, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        this.onBackPressedDispatcher.addCallback(this, callback)
//    }
}