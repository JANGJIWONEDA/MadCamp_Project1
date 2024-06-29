package com.example.project1.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R

class DiaryAdapter (private val itemList: ArrayList<Diaries>): RecyclerView.Adapter<DiaryAdapter.Holder>() {

    interface OnItemClickListener{
        fun onCardViewClick(view: View, diaries: Diaries, pos: Int)
    }
    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener:OnItemClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
        return Holder(view)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        private val diaryName = itemView?.findViewById<TextView>(R.id.diaryName)
        private val cardView = itemView?.findViewById<CardView>(R.id.card_view)

        fun bind(diaries: Diaries){
            diaryName?.text = diaries.diaryName

            cardView?.setOnClickListener{
                listener?.onCardViewClick(itemView, diaries, adapterPosition)
            }
        }
    }
    override fun onBindViewHolder(holder: DiaryAdapter.Holder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}