package com.example.project1.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R

class ContactAdapter(private val itemList: List<Contacts>): RecyclerView.Adapter<ContactAdapter.Holder>() {

    interface OnItemClickListener{
        fun onCardViewClick(view: View, contacts: Contacts, pos: Int)
    }
    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener:OnItemClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent, false)
        return Holder(view)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        private val contactName = itemView?.findViewById<TextView>(R.id.contactName)
        private val phoneNumber = itemView?.findViewById<TextView>(R.id.phoneNumber)
        private val contactRelation = itemView?.findViewById<TextView>(R.id.contactRelation)
        private val cardView = itemView?.findViewById<CardView>(R.id.card_view)

        fun bind(contacts: Contacts){
            contactName?.text = contacts.contactName
            phoneNumber?.text = contacts.phoneNumber
            contactRelation?.text = contacts.contactRelation

            cardView?.setOnClickListener{
                listener?.onCardViewClick(itemView, contacts, adapterPosition)
            }
        }
    }
    override fun onBindViewHolder(holder: ContactAdapter.Holder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}