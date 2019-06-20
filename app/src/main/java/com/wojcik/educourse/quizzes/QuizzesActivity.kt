package com.wojcik.educourse.quizzes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.quizzes.data.MyExamResponse
import com.wojcik.educourse.quizzes.data.Quiz
import kotlinx.android.synthetic.main.activity_quizzes.*
import retrofit2.Call
import retrofit2.Response

class QuizzesActivity : AppCompatActivity() {


    private var quizzesList : ArrayList<Quiz> = ArrayList()
    private var quizzesSearchList : ArrayList<Quiz> = ArrayList()

    private var userId = 0
    private var courseId = 0
    private var categoryId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val b : Bundle = intent.extras!!
        userId = b.getInt("userId")
        courseId = b.getInt("courseId")
        categoryId = b.getInt("categoryId")

        supportActionBar!!.title = b.getString("courseName")

        loadQuizzes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_quiz, menu)
        val searchItem = menu!!.findItem(R.id.quizzes_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                searchQuizzes(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.quitCourse -> {
                quitCourse()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun quitCourse() {
        RetrofitClient.instance.quitCourse(userId, courseId)
            .enqueue(object: retrofit2.Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(applicationContext, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Toast.makeText(applicationContext, "Kurs porzucony", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            })
    }

    fun searchQuizzes(searchText : String) {
        quizzesSearchList.clear()
        for(i in 0 until quizzesList.size) {
            if(quizzesList[i].name.toLowerCase().startsWith(searchText.toLowerCase())) {
                quizzesSearchList.add(quizzesList[i])
            }
        }
        createButtons(quizzesSearchList)
    }

    private fun loadQuizzes() {
        RetrofitClient.instance.getExamsById(courseId)
            .enqueue(object: retrofit2.Callback<List<MyExamResponse>> {
                override fun onFailure(call: Call<List<MyExamResponse>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<MyExamResponse>>, response: Response<List<MyExamResponse>>) {
                    for(i in 0 until response.body()!!.size) {
                        quizzesList.add(Quiz(response.body()!![i].id, response.body()!![i].name, response.body()!![i].level))
                        quizzesSearchList.add(quizzesList[i])
                    }

                    createButtons(quizzesSearchList)
                }
            })

    }

    fun goToQuizField(position: Int) {
        val intent = Intent(applicationContext, QuizzesFieldActivity::class.java)
        val b = Bundle()
        b.putInt("examId", quizzesSearchList[position].id)
        b.putString("name", quizzesSearchList[position].name)
        b.putInt("userId", userId)
        b.putInt("categoryId", categoryId)
        b.putInt("courseId", courseId)
        intent.putExtras(b)
        startActivity(intent)
    }

    private fun createButtons(list : ArrayList<Quiz>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewQuizzes.layoutManager = layoutManager
        recyclerViewQuizzes.removeAllViews()

        val adapter = QuizzesAdapter(this, list)
        recyclerViewQuizzes.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
