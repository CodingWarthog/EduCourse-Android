package com.wojcik.educourse.flashcards

import com.wojcik.educourse.R
import java.util.*

public enum class Language(val lang : String, val locale : Locale, val languageCode : String, val image : Int) {
    POLSKI("Polski", Locale.ROOT, "pl_PL", R.drawable.pl),
    ANGIELSKI("Angielski", Locale.UK, "en_US", R.drawable.gb),
    WLOSKI("Włoski", Locale.ITALIAN, "it_IT", R.drawable.ita),
    FRANCUSKI("Francuski", Locale.FRENCH, "fr_FR", R.drawable.fra),
    NIEMIECKI("Niemiecki", Locale.GERMAN, "de_DE", R.drawable.ger);

    companion object {
        fun getLanguage(languageCode: String) : Language {
           return Language.values().find { it.languageCode == languageCode}?: POLSKI
        }
    }

    override fun toString(): String {
        return lang
    }

}



/*

    fun setFrontImage(language : String): Int {
        when (language) {
            "Polski" -> return R.drawable.pl
            "Angielski" -> return R.drawable.gb
            "Niemiecki" -> return R.drawable.ger
            "Włoski" -> return R.drawable.ita
            "Francuski" -> return R.drawable.fra
        }
        return R.drawable.pl
    }


    var languageList = ArrayList<String>()

    init {
        languageList.add("Polski")
        languageList.add("Angielski")
        languageList.add("Włoski")
        languageList.add("Francuski")
        languageList.add("Niemiecki")
    }

    fun getLanguage(language : String) : Locale {
        when(language) {
            "Angielski" -> return Locale.UK
            "Francuski" -> return Locale.FRENCH
            "Włoski" -> return Locale.ITALIAN
            "Niemiecki" -> return Locale.GERMAN
        }
        return Locale.ROOT
    }

    fun getLanguageCode(language : String) : String {
        when(language) {
            "Angielski" -> return "en_US"
            "Francuski" -> return "fr_FR"
            "Włoski" -> return "it_IT"
            "Niemiecki" -> return "de_DE"
        }
        return "pl_PL"
    }
}*/
