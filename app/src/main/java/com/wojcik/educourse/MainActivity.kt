package com.wojcik.educourse

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.flashcards.FlashcardsActivity
import com.wojcik.educourse.login.activities.LoginActivity
import com.wojcik.educourse.profile.ProfileActivity
import com.wojcik.educourse.profile.data.UserResponse
import com.wojcik.educourse.quizzes.CoursesActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var username = ""
    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val b : Bundle = intent.extras!!
        username = b.getString("username")!!
        setUserId()
    }

    fun changeActivityProfile(view: View) {
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        val b = Bundle()
        b.putString("username", username)
        intent.putExtras(b)
        startActivity(intent)
    }

    fun changeActivityFlashcards(view: View) {
        val intent = Intent(applicationContext, FlashcardsActivity::class.java)
        startActivity(intent)
    }

    fun changeActivityCourses(view: View) {
        val intent = Intent(applicationContext, CoursesActivity::class.java)
        val b = Bundle()
        b.putInt("userId", userId)
        intent.putExtras(b)
        startActivity(intent)
    }

    fun changeActivityMaterials(view: View) {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        Toast.makeText(this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun setUserId() {
        RetrofitClient.instance.getUserByUsername(username)
            .enqueue(object: Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    userId = response.body()!!.id

                }
            })
    }
}
