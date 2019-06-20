package com.wojcik.educourse.flashcards

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wojcik.educourse.R
import com.wojcik.educourse.flashcards.data.FlashcardSet
import kotlinx.android.synthetic.main.flashcard_set_item.view.*

class FlashcardSetAdapter(val context: Context, private val flashcardSets: List<FlashcardSet>) : RecyclerView.Adapter<FlashcardSetAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flashcard_set_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return flashcardSets.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val flashcardSet = flashcardSets[position]
        holder.setData(flashcardSet, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(flashcardSet: FlashcardSet?, pos: Int) {
            itemView.name_txt_card.text = flashcardSet!!.name
            itemView.description_txt_card.text = flashcardSet.description
            itemView.front_flag_image.setImageResource(setFrontImage(flashcardSet.frontLanguage))
            itemView.back_flag_image.setImageResource(setFrontImage(flashcardSet.backLanguage))
            itemView.setOnClickListener {
                (context as FlashcardsActivity).goToFlashcardField(pos)
            }
        }
    }

    fun setFrontImage(language : String): Int {
        return Language.getLanguage(language).image
    }
}