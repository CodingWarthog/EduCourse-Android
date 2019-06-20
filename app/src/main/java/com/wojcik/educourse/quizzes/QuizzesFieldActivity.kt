package com.wojcik.educourse.quizzes

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.quizzes.data.ExamQuestionResponse
import com.wojcik.educourse.quizzes.data.Question
import com.wojcik.educourse.quizzes.data.QuestionResponse
import com.wojcik.educourse.quizzes.data.UpdateExperienceSend
import kotlinx.android.synthetic.main.activity_quizzes_field.*
import kotlinx.android.synthetic.main.activity_quizzes_field.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.util.*

@Suppress("DEPRECATION")
class QuizzesFieldActivity : AppCompatActivity() {

    private var questionList : ArrayList<Question> = ArrayList()
    private val buttonsList : ArrayList<Button> = ArrayList()
    private var counter = 0
    private var quizCompleted = false
    private var resultsOn = false

    private var correctAnswer = 0
    private var wrongAnswer = 0
    private var noAnswer = 0

    private var courseId = 0;
    private var userId = 0
    private var examId = 0
    private var categoryId = 0



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes_field)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val b : Bundle = intent.extras!!
        val value = b.getString("name")
        userId = b.getInt("userId")
        examId = b.getInt("examId")
        categoryId = b.getInt("categoryId")
        courseId = b.getInt("courseId")
        supportActionBar!!.title = value

        initQuestions()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initQuestions() {
        RetrofitClient.instance.getQuestions(examId)
            .enqueue(object: retrofit2.Callback<ExamQuestionResponse> {
                override fun onFailure(call: Call<ExamQuestionResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ExamQuestionResponse>, response: Response<ExamQuestionResponse>) {
                    for(i in 0 until response.body()!!.question.size) {
                        val q = response.body()!!.question[i]
                        questionList.add(Question(
                            q.id, q.question1, q.optionOne, q.optionTwo, q.optionThree, q.optionFour,  q.answer, q.examId
                        ))
                    }
                    if(questionList.size == 0) {
                        onBackPressed()
                        Toast.makeText(applicationContext, "Quiz jest pusty", Toast.LENGTH_SHORT).show()
                        return
                    }
                    questionList.shuffle()
                    initQuiz()
                }
            })
    }

    @SuppressLint("ResourceAsColor")
    fun initQuiz() {
        val menu = findViewById<LinearLayout>(com.wojcik.educourse.R.id.scroll_menu)

        for(i in 0 until questionList.size) {
            val element = Button(this)
            element.text = "Pytanie " + (i+1)
            element.background.colorFilter =
                    PorterDuffColorFilter(ContextCompat.getColor(this, R.color.colorButton), PorterDuff.Mode.MULTIPLY)
            element.setTextColor(application.resources.getColor(R.color.buttonTextColor))
            element.setOnClickListener {
                if(i != counter || resultsOn)
                loadQuestion(i)
            }
            buttonsList.add(element)
            menu.addView(element)
        }

        loadQuestion(counter)
    }

    fun loadQuestion(i : Int) {
        completeBtn.setBackgroundResource(R.color.colorButton)
        //załadowanie layoutu z pytaniem i 4 odpowiedziami
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        //przed załadowaniem pytania wszystkie przyciski zostają odznaczone
        questionLayout.question_group.clearCheck()

        //animacja fade in fade out zawartości pytania
        questionLayout.animate().withLayer()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                //włączenie layoutu z odpowiedzią
                showQuestionLayout()

                fillFieldsWithQuestion(i)
                checkPreviouslySelectedAnswer(i)
                if (quizCompleted) {
                    //kolory tekstu odpowiedzi przywrócone do domyślnego (czarny)
                    resetAnswersColor()
                    highlightCorrectAnswer(i)
                }

                questionLayout.animate().withLayer()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }
            .start()

        buttonsList[counter].setTextColor(application.resources.getColor(R.color.buttonTextColor))
        buttonsList[i].setTextColor(application.resources.getColor(R.color.selectedButtonTextColor))
        counter = i
        updateNavButtons()
    }

    private fun updateNavButtons() {
        prevQuestionBtn.setBackgroundResource(R.color.colorButton)
        nextQuestionBtn.setBackgroundResource(R.color.colorButton)
        if(counter == 0) {
            prevQuestionBtn.setBackgroundResource(R.color.colorDisabledButton)
        }
        else if(counter == questionList.size-1) {
            nextQuestionBtn.setBackgroundResource(R.color.colorDisabledButton)
        }
    }

    private fun resetAnswersColor() {
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        questionLayout.answerA.setTextColor(application.resources.getColor(R.color.quizText))
        questionLayout.answerB.setTextColor(application.resources.getColor(R.color.quizText))
        questionLayout.answerC.setTextColor(application.resources.getColor(R.color.quizText))
        questionLayout.answerD.setTextColor(application.resources.getColor(R.color.quizText))
    }

    private fun fillFieldsWithQuestion(questionIndex: Int) {
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        questionLayout.question_text.text = questionList[questionIndex].questionContent
        questionLayout.answerA.text = questionList[questionIndex].answerA
        questionLayout.answerB.text = questionList[questionIndex].answerB
        questionLayout.answerC.text = questionList[questionIndex].answerC
        questionLayout.answerD.text = questionList[questionIndex].answerD
    }

    private fun highlightCorrectAnswer(questionIndex: Int) {
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        when {
            questionLayout.answerA.text == questionList[questionIndex].correctAnswer -> questionLayout.answerA.setTextColor(application.resources.getColor(R.color.correctAnswerTextColor))
            questionLayout.answerB.text == questionList[questionIndex].correctAnswer -> questionLayout.answerB.setTextColor(application.resources.getColor(R.color.correctAnswerTextColor))
            questionLayout.answerC.text == questionList[questionIndex].correctAnswer -> questionLayout.answerC.setTextColor(application.resources.getColor(R.color.correctAnswerTextColor))
            questionLayout.answerD.text == questionList[questionIndex].correctAnswer -> questionLayout.answerD.setTextColor(application.resources.getColor(R.color.correctAnswerTextColor))
        }
    }

    private fun checkPreviouslySelectedAnswer(questionIndex : Int) {
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        when {
            questionLayout.answerA.text == questionList[questionIndex].userAnswer -> questionLayout.answerA.isChecked = true
            questionLayout.answerB.text == questionList[questionIndex].userAnswer -> questionLayout.answerB.isChecked = true
            questionLayout.answerC.text == questionList[questionIndex].userAnswer -> questionLayout.answerC.isChecked = true
            questionLayout.answerD.text == questionList[questionIndex].userAnswer -> questionLayout.answerD.isChecked = true
        }
    }

    private fun saveAnswer() {
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        val selectedAnswer = questionLayout.question_group.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedAnswer)

        if(radioButton != null) {
            questionList[counter].userAnswer = radioButton.text as String
        }
    }

    fun nextQuestion(view: View) {
        if(counter < questionList.size-1) {
            loadQuestion(counter+1)
            updateScrollPosition()
        }
    }

    fun prevQuestion(view: View) {
        if(counter > 0) {
            loadQuestion(counter-1)
            updateScrollPosition()
        }
    }

    fun checkQuestion(view: View) {
        if(!quizCompleted) {
            buttonsList[counter].background.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, R.color.colorSelectedAnswered), PorterDuff.Mode.MULTIPLY)
            saveAnswer()
        }
    }

    fun completeQuiz(view: View) {
        if(!quizCompleted) {
            val completeDialog = AlertDialog.Builder(this)
            completeDialog.setMessage("Czy na pewno chcesz zakończyć quiz?")
            completeDialog.setCancelable(true)
            completeDialog.setPositiveButton("Tak") { _: DialogInterface, _: Int ->
                quizCompleted = true

                completeBtn.text = "WYNIKI"
                val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
                questionLayout.question_group.isClickable = false

                for (i in 0 until questionList.size) {
                    if (questionList[i].userAnswer == questionList[i].correctAnswer) {
                        buttonsList[i].background.colorFilter = PorterDuffColorFilter(
                            ContextCompat.getColor(this, R.color.correctAnswerButtonColor),
                            PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        buttonsList[i].background.colorFilter = PorterDuffColorFilter(
                            ContextCompat.getColor(this, R.color.wrongAnswerTextColor),
                            PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
                calculateResults()
                fillResultLayout()
                updateExperience()
                showResults()
            }
            completeDialog.setNegativeButton("Nie") { _: DialogInterface, _: Int ->
                //brak akcji
            }

            completeDialog.create().show()
        }
        else if(!resultsOn) {
            showResults()
        }
    }

    private fun updateExperience() {
        val updateExperienceSend = UpdateExperienceSend(categoryId, correctAnswer, userId)

        RetrofitClient.instance.updateExperience(updateExperienceSend, courseId)
            .enqueue(object: Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(applicationContext, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    val points = when {
                        correctAnswer == 1 -> "punkt"
                        correctAnswer <= 4 && correctAnswer > 0 -> "punkty"
                        else -> "punktów"
                    }
                    Toast.makeText(applicationContext, "Zdobyłeś $correctAnswer $points w tej kategorii", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun updateScrollPosition() {
        val hsv = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
        hsv.smoothScrollTo((hsv.getChildAt(0).measuredWidth ) *(counter-1)/questionList.size,0)
    }

    private fun showResults() {
        completeBtn.setBackgroundResource(R.color.colorDisabledButton)
        showResultsLayout()
    }

    private fun showResultsLayout() {
        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        questionLayout.visibility = View.INVISIBLE

        val resultsLayout = findViewById<LinearLayout>(R.id.results_layout)
        resultsLayout.visibility = View.VISIBLE
        resultsOn = true
    }

    private fun showQuestionLayout() {
        val resultsLayout = findViewById<LinearLayout>(R.id.results_layout)
        resultsLayout.visibility = View.INVISIBLE


        val questionLayout = findViewById<LinearLayout>(R.id.question_layout)
        questionLayout.visibility = View.VISIBLE

        resultsOn = false
    }

    private fun calculateResults() {
        for(i in 0 until questionList.size) {
            when {
                questionList[i].userAnswer == "" -> noAnswer++
                questionList[i].userAnswer == questionList[i].correctAnswer -> correctAnswer++
                else -> wrongAnswer++
            }
        }
    }

    private fun getGradeString() : String {
        var grade: String
        val percent = correctAnswer * 100 / questionList.size

        grade = when {
            percent < 30 -> "Porażka"
            percent < 40 -> "Słabo"
            percent < 50 -> "Średnio"
            percent < 70 -> "Dobrze"
            percent < 80 -> "Bardzo dobrze"
            percent < 90 -> "Świetnie"
            else -> "Rewelacyjnie!"
        }
        return grade
    }

    private fun fillResultLayout() {
        val resultsLayout = findViewById<LinearLayout>(R.id.results_layout)

        val sb = StringBuilder()
        sb.append(correctAnswer).append("/").append(questionList.size)
        resultsLayout.points_txt.text = sb.toString()

        resultsLayout.grade_txt.text = getGradeString()

        sb.clear()
        sb.append("Poprawne odpowiedzi: ").append(correctAnswer)
        resultsLayout.correct_answers_txt.text = sb.toString()

        sb.clear()
        sb.append("Niepoprawne odpowiedzi: ").append(wrongAnswer)
        resultsLayout.wrong_answers_txt.text = sb.toString()

        sb.clear()
        sb.append("Bez odpowiedzi: ").append(noAnswer)
        resultsLayout.no_answer_txt.text = sb.toString()

    }






}
