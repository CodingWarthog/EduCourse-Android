package com.wojcik.educourse.login.activities

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.wojcik.educourse.MainActivity
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.login.models.LoginResponse
import com.wojcik.educourse.login.models.LoginSend
import com.wojcik.educourse.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.PrintWriter


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        //reset tokenu
        RetrofitClient.TOKEN = ""

        loginTxt.setText("pawel95")
        passwordTxt.setText("1234")
    }

    //button zaloguj
    fun loginFun(view: View){

        val login = loginTxt.text.toString().trim()
        val password = passwordTxt.text.toString().trim()

        if( login.isEmpty() ){
            loginTxt.error = "Wpisz login"
            loginTxt.requestFocus()
            return
        }
        if( password.isEmpty() ){
            passwordTxt.error = "Wpisz hasło"
            passwordTxt.requestFocus()
            return
        }

        loginBtn.isClickable = false
        progressLogin.visibility = View.VISIBLE
        Utils.hideSoftKeyBoard(this, view )

        //tworzenie obiektu do wysłania
        val loginSend = LoginSend(login,password)

        RetrofitClient.instance.login( loginSend )
            .enqueue(object: Callback<LoginResponse> {

                override fun onFailure(call: Call<LoginResponse>, t: Throwable){
                    Toast.makeText(applicationContext, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
                    loginBtn.isClickable = true
                    progressLogin.visibility = View.INVISIBLE
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>){

                    when(response.code()){
                        //Unauthorized
                        401 -> {
                            Toast.makeText(applicationContext, "Zły login lub hasło", Toast.LENGTH_LONG).show()
                            loginTxt.text.clear()
                            passwordTxt.text.clear()
                            loginBtn.isClickable = true
                            progressLogin.visibility = View.INVISIBLE
                        }

                        //OK
                        200 -> {
                            //zapis otrzymanego tokena
                            RetrofitClient.TOKEN = response.body()?.token.toString()

                            //stworzenie activity po udanym logowaniu
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            val b = Bundle()
                            b.putString("username", loginTxt.text.toString())
                            intent.putExtras(b)

                            //otworzenie nowego activity
                            startActivity(intent)
                            progressLogin.visibility = View.INVISIBLE
                        }
                        //inny kod odpowiedzi serwera
                        else ->{
                            Toast.makeText(applicationContext, "Błąd serwera - spróbuj później", Toast.LENGTH_LONG).show()
                            loginBtn.isClickable = true
                            progressLogin.visibility = View.INVISIBLE
                        }
                    }
                }
            })
    }

    //button zarejestruj
    fun registerFun(view: View){
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    object Utils {

        fun hideSoftKeyBoard(context: Context, view: View) {
            try {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                // TODO: handle exception
                e.printStackTrace()
            }

        }
    }

}
