package com.wojcik.educourse.dbHelper

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.wojcik.educourse.flashcards.data.Flashcard
import com.wojcik.educourse.flashcards.data.FlashcardSet
import java.lang.Exception

class FlashcardDAOImpl (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER){
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FLASHCARDSET_QUERY : String  = ("CREATE TABLE $TABLE_NAME_FLASHCARDSET($COL_ID_FLASHCARDSET INTEGER PRIMARY KEY autoincrement,$COL_NAME_FLASHCARDSET TEXT,$COL_DESCRIPTION_FLASHCARDSET TEXT, $COL_FRONT_LANGUAGE TEXT, $COL_BACK_LANGUAGE TEXT)")
        db!!.execSQL(CREATE_FLASHCARDSET_QUERY)

        val CREATE_FLASHCARD_QUERY : String  = ("CREATE TABLE $TABLE_NAME_FLASHCARD($COL_ID_FLASHCARD INTEGER PRIMARY KEY autoincrement,$COL_FRONT_FLASHCARD TEXT,$COL_BACK_FLASHCARD TEXT,$COL_SET_FLASHCARD INTEGER)")
        db.execSQL(CREATE_FLASHCARD_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_FLASHCARD")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_FLASHCARDSET")
        onCreate(db)
    }

    companion object {
        private val DATABASE_VER = 19
        private val DATABASE_NAME = "EduCourseDB.db"

        //Table
        private val TABLE_NAME_FLASHCARDSET = "FlashcardsSet"
        private val COL_ID_FLASHCARDSET = "Id"
        private val COL_NAME_FLASHCARDSET = "Name"
        private val COL_DESCRIPTION_FLASHCARDSET = "Desription"
        private val COL_FRONT_LANGUAGE = "FrontLanguage"
        private val COL_BACK_LANGUAGE = "BackLanguage"

        private val TABLE_NAME_FLASHCARD = "Flashcards"
        private val COL_ID_FLASHCARD = "Id"
        private val COL_FRONT_FLASHCARD = "Front"
        private val COL_BACK_FLASHCARD = "Back"
        private val COL_SET_FLASHCARD = "FlashcardsSet"
    }

    fun addFlashcard(flashcard: Flashcard)
    {
        val db : SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID_FLASHCARD, flashcard.id)
        values.put(COL_FRONT_FLASHCARD, flashcard.front)
        values.put(COL_BACK_FLASHCARD, flashcard.back)
        values.put(COL_SET_FLASHCARD, flashcard.set)

        db.insert(TABLE_NAME_FLASHCARD, null, values)
        db.close()
    }

    fun updateFlashcard(flashcard: Flashcard) : Int
    {
        val db : SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID_FLASHCARD, flashcard.id)
        values.put(COL_FRONT_FLASHCARD, flashcard.front)
        values.put(COL_BACK_FLASHCARD, flashcard.back)
        values.put(COL_SET_FLASHCARD, flashcard.set)

        return db.update(TABLE_NAME_FLASHCARD, values, "$COL_ID_FLASHCARD=?", arrayOf(flashcard.id.toString()))
    }

    fun deleteFlashcard(flashcard: Flashcard) {
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_NAME_FLASHCARD, "$COL_ID_FLASHCARD=?", arrayOf(flashcard.id.toString()))
        db.close()
    }

    fun flashcardById(id: Int) : Flashcard {
        val selectQuery = "SELECT * FROM $TABLE_NAME_FLASHCARD WHERE $COL_ID_FLASHCARD = $id"
        val db : SQLiteDatabase = this.writableDatabase
        val cursor : Cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst())
        {
                val flashcardId = cursor.getInt(cursor.getColumnIndex(COL_ID_FLASHCARD))
                val front = cursor.getString(cursor.getColumnIndex(COL_FRONT_FLASHCARD))
                val back = cursor.getString(cursor.getColumnIndex(COL_BACK_FLASHCARD))
                val set = cursor.getInt(cursor.getColumnIndex(COL_SET_FLASHCARD))
                db.close()
                return Flashcard(flashcardId, front, back, set)
        }
        db.close()
        return Flashcard(null, "", "", -1)
    }

    fun allFlashcardsById(id: Int) : List<Flashcard> {
        val listFlashcards = ArrayList<Flashcard>()
        val selectQuery = "SELECT * FROM $TABLE_NAME_FLASHCARD WHERE $COL_SET_FLASHCARD = $id"
        val db : SQLiteDatabase = this.writableDatabase
        val cursor : Cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst())
        {
            do {
                val flashcardId = cursor.getInt(cursor.getColumnIndex(COL_ID_FLASHCARD))
                val front = cursor.getString(cursor.getColumnIndex(COL_FRONT_FLASHCARD))
                val back = cursor.getString(cursor.getColumnIndex(COL_BACK_FLASHCARD))
                val set = cursor.getInt(cursor.getColumnIndex(COL_SET_FLASHCARD))
                val flashcard = Flashcard(flashcardId, front, back, set)

                listFlashcards.add(flashcard)
            }while (cursor.moveToNext())
        }
        db.close()
        return listFlashcards
    }

    //FlashcardSet

    val allFlashcardSets : List<FlashcardSet>
        get() {
            val listFlashcardSets = ArrayList<FlashcardSet>()
            val selectQuery = "SELECT * FROM $TABLE_NAME_FLASHCARDSET"
            val db : SQLiteDatabase = this.writableDatabase
            val cursor : Cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst())
            {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COL_ID_FLASHCARDSET))
                    val name = cursor.getString(cursor.getColumnIndex(COL_NAME_FLASHCARDSET))
                    val description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION_FLASHCARDSET))
                    val frontLanguage = cursor.getString(cursor.getColumnIndex(COL_FRONT_LANGUAGE))
                    val backLanguage = cursor.getString(cursor.getColumnIndex(COL_BACK_LANGUAGE))
                    val flashcardSet = FlashcardSet(id, name, description, frontLanguage, backLanguage)

                    listFlashcardSets.add(flashcardSet)
                }while (cursor.moveToNext())
            }
            db.close()
            return listFlashcardSets
        }

    fun flashcardSetById(id: Int) : FlashcardSet {
        val selectQuery = "SELECT * FROM $TABLE_NAME_FLASHCARDSET WHERE $COL_ID_FLASHCARDSET = $id"
        val db : SQLiteDatabase = this.writableDatabase
        val cursor : Cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst())
        {
            val id = cursor.getInt(cursor.getColumnIndex(COL_ID_FLASHCARDSET))
            val name = cursor.getString(cursor.getColumnIndex(COL_NAME_FLASHCARDSET))
            val description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION_FLASHCARDSET))
            val frontLanguage = cursor.getString(cursor.getColumnIndex(COL_FRONT_LANGUAGE))
            val backLanguage = cursor.getString(cursor.getColumnIndex(COL_BACK_LANGUAGE))
            val flashcardSet = FlashcardSet(id, name, description, frontLanguage, backLanguage)
            db.close()
            return flashcardSet
        }
        db.close()
        return FlashcardSet(null, "", "", "", "")
    }

    fun addFlashcardSet(flashcardSet: FlashcardSet)
    {
        val db : SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID_FLASHCARDSET, flashcardSet.id)
        values.put(COL_NAME_FLASHCARDSET, flashcardSet.name)
        values.put(COL_DESCRIPTION_FLASHCARDSET, flashcardSet.description)
        values.put(COL_FRONT_LANGUAGE, flashcardSet.frontLanguage)
        values.put(COL_BACK_LANGUAGE, flashcardSet.backLanguage)

        db.insert(TABLE_NAME_FLASHCARDSET, null, values)
        db.close()
    }

    fun updateFlashcardSet(flashcardSet: FlashcardSet) : Int
    {
        val db : SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID_FLASHCARDSET, flashcardSet.id)
        values.put(COL_NAME_FLASHCARDSET, flashcardSet.name)
        values.put(COL_DESCRIPTION_FLASHCARDSET, flashcardSet.description)
        values.put(COL_FRONT_LANGUAGE, flashcardSet.frontLanguage)
        values.put(COL_BACK_LANGUAGE, flashcardSet.backLanguage)

        return db.update(TABLE_NAME_FLASHCARDSET, values, "$COL_ID_FLASHCARDSET=?", arrayOf(flashcardSet.id.toString()))
    }

    fun deleteFlashcardSetById(id: Int) {
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_NAME_FLASHCARDSET, "$COL_ID_FLASHCARDSET=?", arrayOf(id.toString()))
        db.close()
    }
}