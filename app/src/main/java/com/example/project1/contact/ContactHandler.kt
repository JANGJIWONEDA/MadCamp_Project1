package com.example.project1.contact

import android.content.Context
import org.json.JSONArray
import org.json.JSONTokener
import java.io.File

class ContactHandler(val context: Context?) {
    fun getContactsList(): ArrayList<Contacts> {
        val contactJsonFile = File(context!!.filesDir, "contacts.json")
        val contactsList = ArrayList<Contacts>()

        if(contactJsonFile.exists()) {
            val contactJsonString = contactJsonFile.readText()

            if (contactJsonString.isNotEmpty()) {
                val contactsJsonArray = JSONTokener(contactJsonString).nextValue() as JSONArray
                for(i in 0 until contactsJsonArray.length()){
                    val name = contactsJsonArray.getJSONObject(i).getString("contactName")
                    val phoneNumber = contactsJsonArray.getJSONObject(i).getString("phoneNumber")
                    val relationship = contactsJsonArray.getJSONObject(i).getString("contactRelation")
                    val tag1 = contactsJsonArray.getJSONObject(i).getString("contactTag1")
                    val tag2 = contactsJsonArray.getJSONObject(i).getString("contactTag2")
                    val tag3 = contactsJsonArray.getJSONObject(i).getString("contactTag3")
                    contactsList.add(Contacts(name, phoneNumber, relationship, tag1, tag2, tag3))
                }
            }
        }
        return contactsList
    }
    fun writeContactList(newData: String) {
        val diaryJsonFile = File(context!!.filesDir, "contacts.json")
        if(!diaryJsonFile.exists()){
            context.openFileOutput("contacts.json", Context.MODE_PRIVATE)
        }
        diaryJsonFile.writeText(newData)

    }
}