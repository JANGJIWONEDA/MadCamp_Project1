package com.example.project1.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.Memo.adapter.NoteAdapter.NoteViewHolder
import com.example.project1.R
import com.example.project1.databinding.NoteLayoutBinding

class DiaryAdapter (): RecyclerView.Adapter<DiaryAdapter.Holder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Diaries>() {
        override fun areItemsTheSame(oldItem: Diaries, newItem: Diaries): Boolean {
            return oldItem.id == newItem.id // ID가 같은지 여부로 항목 비교
        }

        override fun areContentsTheSame(oldItem: Diaries, newItem: Diaries): Boolean {
            return oldItem.diaryName == newItem.diaryName // 내용이 같은지 여부로 항목 비교
        }
    }

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
    val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: DiaryAdapter.Holder, position: Int) {
        val itemList = differ.currentList
        holder.bind(itemList[position])
    }
    fun submitList(list: List<Diaries>) {
        differ.submitList(list)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
