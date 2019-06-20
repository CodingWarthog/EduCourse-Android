package com.wojcik.educourse.flashcards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.dbHelper.FlashcardDAOImpl
import com.wojcik.educourse.flashcards.data.Flashcard
import kotlinx.android.synthetic.main.activity_flashcard_creator.*

class FlashcardCreator : AppCompatActivity() {

    private var setId = 0
    private var name = ""
    private lateinit var db: FlashcardDAOImpl
    private var flashcardId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_creator)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        db = FlashcardDAOImpl(this)

        val myIntent = intent
        setId = myIntent.getIntExtra("setId", 0)
        name = myIntent.getStringExtra("name")


        flashcardId = myIntent.getIntExtra("flashcardId", 0)
        if(flashcardId != 0) {
            val flashcard = db.flashcardById(flashcardId)
            set_name.setText(flashcard.front)
            back_txt.setText(flashcard.back)
            add_flashcard_btn.setText(R.string.edit)
            set_title.setText(R.string.edit_current_flashcard)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun addFlashcard(view: View) {
        if(flashcardId == 0) {
            db.addFlashcard(Flashcard(null, set_name.text.toString(), back_txt.text.toString(), setId))
            Toast.makeText(this, R.string.flashcard_added, Toast.LENGTH_SHORT).show()
            set_name.text.clear()
            back_txt.text.clear()
            set_name.requestFocus()
        }
        else {
            db.updateFlashcard(Flashcard(flashcardId, set_name.text.toString(), back_txt.text.toString(), setId))
            Toast.makeText(this, R.string.flashcard_edited, Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}
