package com.wojcik.educourse.flashcards

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.dbHelper.FlashcardDAOImpl
import com.wojcik.educourse.flashcards.data.Flashcard
import kotlinx.android.synthetic.main.activity_flashcards_field.*
import java.util.*

class FlashcardsFieldActivity : AppCompatActivity(){
    private val REQUEST_CODE_SPEECH_INPUT = 100

    private var flashcardList : ArrayList<Flashcard> = ArrayList()
    private var counter : Int = 0
    private var setId : Int = 0
    private var name : String = ""
    private var description : String = ""
    private var frontLanguage : String = ""
    private var backLanguage : String = ""
    private var tts : TextToSpeech? = null

    private lateinit var db:FlashcardDAOImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards_field)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        db = FlashcardDAOImpl(this)
        init()

    }

    private fun init() {
        setLanguage()
        val b : Bundle = intent.extras!!
        setId = b.getInt("id")
        loadData()
        supportActionBar!!.title = name
        val tempList = db.allFlashcardsById(setId)

        flashcardList.clear()
        flashcardList.addAll(tempList)

        updateInfo()
        if(flashcardList.size > 0) {
            if(flashcardText.text != flashcardList[counter].back) {
                flashcardText.text = flashcardList[counter].front
                flashcardText.setBackgroundResource(R.color.flashcardFront)
            }
            speak_btn.setBackgroundResource(R.color.colorButton)
        }
        else {
            flashcardText.setText(R.string.set_is_empty)
            flashcardText.setBackgroundResource(R.color.flashcardEmpty)
        }
    }

    private fun setLanguage() {
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                if(reverseSwitch.isChecked) {
                    tts!!.language = Language.getLanguage(frontLanguage).locale
                }
                else {
                    tts!!.language = Language.getLanguage(backLanguage).locale
                }

            }
        })
    }

    fun onSwitchClick(view : View) {
        setLanguage()
    }

    private fun loadData() {
        val flashcardSet = db.flashcardSetById(setId)
        name = flashcardSet.name
        description = flashcardSet.description
        frontLanguage = flashcardSet.frontLanguage
        backLanguage = flashcardSet.backLanguage
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcards_field, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.addFlashcard -> {
                addFlashcard()
            }
            R.id.editSet -> {
                editSet()
            }
            R.id.editFlashcard -> {
                editFlashcard()
            }
            R.id.deleteFlashcard -> {
                deleteFlashcard()
            }
            R.id.deleteFlashcardSet -> {
                deleteFlashcardSet()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("SetTextI18n")
    fun updateInfo() {
        if(flashcardList.size > 0) {
            flashcardInfo.text = (counter + 1).toString() + "/" + (flashcardList.size)
        }
        else {
            flashcardInfo.text = ""
        }
    }


    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }



    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun flipCard(view: View) {
        flip(false)
    }

    private fun getReverseText() : String {
        return if(flashcardText.text == flashcardList[counter].front) {
            flashcardList[counter].front
        } else {
            flashcardList[counter].back
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun flip(isVoice : Boolean) {
        if(flashcardList.size > 0) {
            flashcardText.animate().withLayer()
                .rotationY(90f)
                .setDuration(300)
                .withEndAction {
                    flipCard()
                    flashcardText.rotationY = -90f
                    flashcardText.animate().withLayer()
                        .rotationY(0f)
                        .setDuration(300)
                        .start()
                    if(!isVoice && ((reverseSwitch.isChecked && flashcardText.text == flashcardList[counter].front) ||
                        (!reverseSwitch.isChecked && flashcardText.text == flashcardList[counter].back))) {
                        tts!!.speak(getReverseText(), TextToSpeech.QUEUE_FLUSH, null, "")
                    }
                }
                .start()
        }
    }

    fun prevFlashcard(view : View) {
        if(flashcardList.size > 0) {
            if (counter > 0) {
                counter--
            } else {
                counter = flashcardList.size - 1
            }
            fadeInOut()
        }
    }

    fun nextFlashcard(view : View) {
        if(flashcardList.size > 0) {
            if (counter < flashcardList.size - 1) {
                counter++
            } else {
                counter = 0
            }
            fadeInOut()
        }
    }

    fun shuffle(view : View) {
        if(flashcardList.size > 0) {
            flashcardList.shuffle()
            counter = 0
            fadeInOut()
        }
    }

    private fun flipCard() {
        if(flashcardList.size > 0) {
            if (flashcardText.text == flashcardList[counter].front) {
                flashcardText.text = flashcardList[counter].back
                flashcardText.setBackgroundResource(R.color.flashcardBack)
            } else {
                flashcardText.text = flashcardList[counter].front
                flashcardText.setBackgroundResource(R.color.flashcardFront)
            }
        }
    }

    private fun fadeInOut() {
        if(flashcardList.size > 0) {
            flashcardText.animate().withLayer()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    if (!reverseSwitch.isChecked) {
                        flashcardText.text = flashcardList[counter].front
                        flashcardText.setBackgroundResource(R.color.flashcardFront)
                    } else {
                        flashcardText.text = flashcardList[counter].back
                        flashcardText.setBackgroundResource(R.color.flashcardBack)
                    }
                    updateInfo()
                    flashcardText.animate().withLayer()
                        .alpha(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
        }
    }

    private fun addFlashcard() {
        val intent = Intent(this, FlashcardCreator::class.java)
        intent.putExtra("setId", setId)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    private fun editSet() {
        val intent = Intent(this, FlashcardsSetCreator::class.java)
        intent.putExtra("setId", setId)
        intent.putExtra("name", name)
        intent.putExtra("description", description)
        intent.putExtra("frontLanguage", frontLanguage)
        intent.putExtra("backLanguage", backLanguage)
        startActivity(intent)
    }

    private fun editFlashcard() {
        if(flashcardList.size == 0) {
            Toast.makeText(this, R.string.set_is_empty, Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this, FlashcardCreator::class.java)
            intent.putExtra("setId", setId)
            intent.putExtra("name", name)

            intent.putExtra("flashcardId", flashcardList[counter].id)
            startActivity(intent)
        }
    }

    private fun deleteFlashcard() {
        if(flashcardList.size == 0) {
            Toast.makeText(this, R.string.set_is_empty, Toast.LENGTH_SHORT).show()
        }
        else {
            val completeDialog = AlertDialog.Builder(this)
            completeDialog.setMessage(R.string.delete_flashcard)
            completeDialog.setCancelable(true)
            completeDialog.setPositiveButton(R.string.dialog_positive) { _: DialogInterface, _: Int ->
                db.deleteFlashcard(flashcardList[counter])
                flashcardList.removeAt(counter)
                if (counter > 0) {
                    counter--
                }
                init()
            }
            completeDialog.setNegativeButton(R.string.dialog_negative) { _: DialogInterface, _: Int ->

            }
            completeDialog.create().show()
        }
    }

    private fun deleteFlashcardSet() {
        val completeDialog = AlertDialog.Builder(this)
        completeDialog.setMessage(R.string.delete_set)
        completeDialog.setCancelable(true)
        completeDialog.setPositiveButton(R.string.dialog_positive) { _: DialogInterface, _: Int ->
            db.deleteFlashcardSetById(setId)
            Toast.makeText(this, R.string.set_deleted, Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
        completeDialog.setNegativeButton(R.string.dialog_negative) { _: DialogInterface, _: Int ->
        }

        completeDialog.create().show()
    }

    fun speak(view: View) {
        if(flashcardList.size > 0) {
            val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getReverseFlashcardLanguage())
            mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            //mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speak)

            try {
                startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getReverseFlashcardLanguage() : String {
        if((reverseSwitch.isChecked && !getCurrentSide()) || (!reverseSwitch.isChecked && !getCurrentSide())) {
            return Language.getLanguage(frontLanguage).languageCode
        }
        else {
            return Language.getLanguage(backLanguage).languageCode
        }
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if(resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    Toast.makeText(this, result[0].toString(), Toast.LENGTH_SHORT).show()
                    var word = if(getCurrentSide()) {
                        flashcardList[counter].back

                    } else {
                        flashcardList[counter].front
                    }
                    if(word.indexOf('(') != -1) {
                        word = word.substring(0, word.indexOf('('))
                    }
                    if (result[0].toString().toLowerCase().replace(" ", "") == word.toLowerCase().replace(" ", "")) {
                        flip(true)
                    }
                }
            }
        }
    }

    private fun getCurrentSide() : Boolean {
        return flashcardText.text == flashcardList[counter].front
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
