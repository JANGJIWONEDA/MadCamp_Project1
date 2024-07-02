package com.example.project1.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R

class ContactAdapter(private var itemList: List<Contacts>) : RecyclerView.Adapter<ContactAdapter.Holder>() {

    interface OnItemClickListener {
        fun onCardViewClick(view: View, contacts: Contacts, pos: Int)
        fun onCallClick(view: View, contacts: Contacts, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateContactList(newContactList: List<Contacts>) {
        itemList = newContactList.sortedBy { it.contactName }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent, false)
        return Holder(view)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView = itemView.findViewById(R.id.contactName)
        private val phoneNumber: TextView = itemView.findViewById(R.id.phoneNumber)
        private val contactRelation: TextView = itemView.findViewById(R.id.contactRelation)
        private val cardView: CardView = itemView.findViewById(R.id.card_view)
        private val callbtn: Button = itemView.findViewById(R.id.call_point)

        fun bind(contacts: Contacts) {
            contactName.text = contacts.contactName
            phoneNumber.text = contacts.phoneNumber
            contactRelation.text = contacts.contactRelation
            cardView.setOnClickListener {
                listener?.onCardViewClick(itemView, contacts, adapterPosition)
            }
            callbtn.setOnClickListener {
                listener?.onCallClick(itemView, contacts, adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
