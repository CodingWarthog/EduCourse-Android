package com.wojcik.educourse.flashcards

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.SearchView
import com.wojcik.educourse.dbHelper.FlashcardDAOImpl
import com.wojcik.educourse.flashcards.data.FlashcardSet

import com.wojcik.educourse.R
import kotlinx.android.synthetic.main.activity_flashcards.*
import kotlinx.android.synthetic.main.flashcard_set_item.view.*


class FlashcardsActivity : AppCompatActivity() {

    private lateinit var db: FlashcardDAOImpl
    private var flashcardSetList : ArrayList<FlashcardSet> = ArrayList()
    private var flashcardSetSearchList : ArrayList<FlashcardSet> = ArrayList()
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wojcik.educourse.R.layout.activity_flashcards)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        db = FlashcardDAOImpl(this)
        this.loadFlashcards()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcards, menu)
        val searchItem = menu!!.findItem(R.id.flashcards_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                searchSets(newText)
                searchText = newText
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        })
        return true
    }

    private fun searchSets(searchText: String) {
        flashcardSetSearchList.clear()
        for(i in 0 until flashcardSetList.size) {
            if(flashcardSetList[i].name.toLowerCase().startsWith(searchText.toLowerCase()) || flashcardSetList[i].description.toLowerCase().startsWith(searchText.toLowerCase())) {
                flashcardSetSearchList.add(flashcardSetList[i])
            }
        }
        createButtons(flashcardSetSearchList)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.addSet -> {
                val intent = Intent(applicationContext, FlashcardsSetCreator::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFlashcards() {
        flashcardSetList.clear()
        flashcardSetList.addAll(db.allFlashcardSets)
        this.createButtons(flashcardSetList)
    }

    @SuppressLint("InflateParams")
    private fun createButtons(list : ArrayList<FlashcardSet>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewFlashcards.layoutManager = layoutManager
        recyclerViewFlashcards.removeAllViews()

        val adapter = FlashcardSetAdapter(this, list)
        recyclerViewFlashcards.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        this.loadFlashcards()
        this.searchSets(searchText)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun goToFlashcardField(position: Int) {
        val intent = Intent(applicationContext, FlashcardsFieldActivity::class.java)
        val b = Bundle()
        b.putInt("id", flashcardSetSearchList[position].id!!)
        intent.putExtras(b)
        startActivity(intent)
    }


}
