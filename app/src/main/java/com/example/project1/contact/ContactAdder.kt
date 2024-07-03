package com.example.project1.contact

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson
import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.repository.DiaryRepository

class ContactAdder : AppCompatActivity() {
    private lateinit var autoCompleteTextView1: AutoCompleteTextView
    private lateinit var autoCompleteTextView2: AutoCompleteTextView
    private lateinit var autoCompleteTextView3: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_adder)

        val diaryRepository = DiaryRepository(DiaryDatabase(this))
        diaryRepository.getAllDiaryTags().observe(this, Observer { tags ->
            tags?.let {
                autoCompleteTextView1 = findViewById(R.id.add_tag1)
                autoCompleteTextView2 = findViewById(R.id.add_tag2)
                autoCompleteTextView3 = findViewById(R.id.add_tag3)

                autoCompleteTextView1.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        tags
                    )
                )

                autoCompleteTextView2.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        tags
                    )
                )
                autoCompleteTextView3.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        tags
                    )
                )
            }
        })
    }

        fun onClickAddContactButton(view: View) {
            val ch = ContactHandler(applicationContext)
            val contactList = ch.getContactsList()

            var newContactPhone = findViewById<EditText>(R.id.add_phone).text.toString()
            var newContactName = findViewById<EditText>(R.id.add_name).text.toString()
            val newContactRelation = findViewById<EditText>(R.id.add_relation).text.toString()
            val newTag1 = findViewById<EditText>(R.id.add_tag1).text.toString()
            val newTag2 = findViewById<EditText>(R.id.add_tag2).text.toString()
            val newTag3 = findViewById<EditText>(R.id.add_tag3).text.toString()
            if (newContactPhone == "") {
                newContactPhone = "00000000000"
            }
            if (newContactName == "") {
                newContactName = "noName"
            }
            for (contact in contactList) {
                if (contact.contactName == newContactName) {
                    Toast.makeText(applicationContext, "이 사람은 이미 저장되어 있습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
            }

            val newContact = Contacts(
                newContactName,
                newContactPhone,
                newContactRelation,
                newTag1,
                newTag2,
                newTag3
            )
            contactList.add(newContact)

            val gson = Gson()
            val newContactsListJson: String = gson.toJson(contactList)
            ch.writeContactList(newContactsListJson)

            val intent = Intent(this@ContactAdder, MainActivity::class.java)
            startActivity(intent)
        }

}