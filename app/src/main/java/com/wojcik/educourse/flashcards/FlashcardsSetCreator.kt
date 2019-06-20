package com.wojcik.educourse.flashcards

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.dbHelper.FlashcardDAOImpl
import com.wojcik.educourse.flashcards.data.FlashcardSet
import kotlinx.android.synthetic.main.activity_flashcards_set_creator.*

class FlashcardsSetCreator : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var setId : Int? = null
    var setName : String? = null
    var setDescription : String? = null
    private var frontLanguage : String? = null
    private var backLanguage : String? = null
    lateinit var language : Language

    private lateinit var db : FlashcardDAOImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards_set_creator)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        db = FlashcardDAOImpl(this)
        addItemsToSpinners()
        init()

    }

    private fun init() {
        if(intent.extras != null) {
            val b: Bundle = intent.extras!!
            setId = b.getInt("setId")
            setName = b.getString("name")
            setDescription = b.getString("description")
            frontLanguage = b.getString("frontLanguage")
            backLanguage = b.getString("backLanguage")


            if (setId != null && setName != null && setDescription != null) {
                set_title.setText(R.string.edit_set)
                set_name.setText(setName.toString())
                set_description.setText(setDescription.toString())

                val dataAdapter = ArrayAdapter<Language>(this, android.R.layout.simple_list_item_1, Language.values())
                front_language_spinner.setSelection(dataAdapter.getPosition(Language.getLanguage(frontLanguage.toString())))
                back_language_spinner.setSelection(dataAdapter.getPosition(Language.getLanguage(backLanguage.toString())))
            }
        }
    }

    private fun addItemsToSpinners() {
        val dataAdapter = ArrayAdapter<Language>(this, android.R.layout.simple_list_item_1, Language.values())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        front_language_spinner.adapter = dataAdapter
        front_language_spinner.onItemSelectedListener = this
        back_language_spinner.adapter = dataAdapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun addNewSet(view: View) {
        val name = set_name.text.toString()
        val desc = set_description.text.toString()

        val frontLanguage = (front_language_spinner.selectedItem as Language).languageCode
        val backLanguage = (back_language_spinner.selectedItem as Language).languageCode

        if(setId == null) {
            val flashcardSet = FlashcardSet(null, name, desc, frontLanguage, backLanguage)
            db.addFlashcardSet(flashcardSet)
            Toast.makeText(this, R.string.set_added, Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, FlashcardsActivity::class.java)
            startActivity(intent)
        }
        else {
            val flashcardSet = FlashcardSet(setId, name, desc, frontLanguage, backLanguage)
            db.updateFlashcardSet(flashcardSet)
            Toast.makeText(this, R.string.data_updated, Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}
