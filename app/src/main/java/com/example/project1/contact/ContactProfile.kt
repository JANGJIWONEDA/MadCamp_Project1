package com.example.project1.contact

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.MainActivity
import com.example.project1.R
import com.google.gson.Gson
import org.w3c.dom.Text

class ContactProfile : AppCompatActivity() {
    private lateinit var profileName2: TextView
    private lateinit var profileName3: TextView
    private lateinit var profileName4: TextView
    private lateinit var profilePhone: TextView
    private lateinit var profileRelation: TextView
    private lateinit var finishButton : Button
    private lateinit var editButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_profile)

        val data = intent.getStringArrayListExtra("contactprofile")
        profileName2 = findViewById(R.id.detailName2)
        profileName3 = findViewById(R.id.detailName3)
        profileName4 = findViewById(R.id.detailName4)
        profilePhone = findViewById(R.id.detailPhone)
        profileRelation = findViewById(R.id.detailRelation)

        profileName2.text = data?.get(0)
        profileName3.text = data?.get(0)
        profileName4.text = data?.get(0)
        profilePhone.text = data?.get(1)
        profileRelation.text = data?.get(2)
        
        finishButton = findViewById(R.id.end_point)
        
        finishButton.setOnClickListener{
            onClickDeleteContactButton(it)
        }

        editButton = findViewById(R.id.edit_point)

        editButton.setOnClickListener{
            onClickEditContactButton(it)
        }
    }
    private fun onClickDeleteContactButton(view: View){
        val ch = ContactHandler(applicationContext)
        val contactList = ch.getContactsList()

        val contactName = findViewById<TextView>(R.id.detailName2).text.toString()
        val filteredContactList = contactList.filter{ con -> con.contactName != contactName}

        val gson = Gson()
        val newContactListJson: String = gson.toJson(filteredContactList)

        ch.writeContactList(newContactListJson)

        Toast.makeText(applicationContext, "$contactName 삭제했습니다.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@ContactProfile, MainActivity::class.java)
        startActivity(intent)
    }
    private fun onClickEditContactButton(view: View){
        val contactsFragment = this
        val intent = Intent(this@ContactProfile, ContactEditer::class.java)
        val name = findViewById<TextView>(R.id.detailName2).text.toString()
        val phone = findViewById<TextView>(R.id.detailPhone).text.toString()
        val relation = findViewById<TextView>(R.id.detailRelation).text.toString()
        intent.putStringArrayListExtra("contactedit", arrayListOf(name, phone, relation))
        startActivity(intent)
    }

//    private fun addOnBackPressedCallback() {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                val intent = Intent(this@ContactProfile, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        this.onBackPressedDispatcher.addCallback(this, callback)
//    }
}