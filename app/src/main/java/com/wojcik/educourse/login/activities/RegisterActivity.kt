package com.wojcik.educourse.login.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.login.models.RegisterSend
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun registerBtnFun(view: View){

        val login = loginRegisterTxt.text.toString().trim()
        val email  = emailRegisterTxt.text.toString().trim()
        val  password: String = passwordRegisterTxt.text.toString().trim()
        val firstName = firstNameRegisterTxt.text.toString().trim()
        val lastName = lastNameRegisterTxt.text.toString().trim()

        if( login.isEmpty() ){
            loginRegisterTxt.error = "Podaj login"
            loginRegisterTxt.requestFocus()
            return
        }

        if( password.isEmpty() ){
            passwordRegisterTxt.error = "Podaj hasło"
            passwordRegisterTxt.requestFocus()
            return
        }

        if( password.length < 4 ){
            passwordRegisterTxt.error = "Hasło musi mieć minimum 4 znaki"
            passwordRegisterTxt.requestFocus()
            return
        }

        if( email.isEmpty() ){
            emailRegisterTxt.error = "Podaj email"
            emailRegisterTxt.requestFocus()
            return
        }

        if(firstName.isEmpty()) {
            firstNameRegisterTxt.error = "Podaj imię"
            firstNameRegisterTxt.requestFocus()
            return
        }

        if(lastName.isEmpty()) {
            lastNameRegisterTxt.error = "Podaj nazwisko"
            lastNameRegisterTxt.requestFocus()
            return
        }

        //tworzenie obiektu do wysłania
        val registerSend = RegisterSend(login, email, password, firstName, lastName)

        RetrofitClient.instance.register( registerSend )
            .enqueue(object: Callback<Unit> {

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(applicationContext, "Brak połączenia z internetem", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>){

                    when(response.code()){
                        //Bad request - user juz jest
                        400 -> Toast.makeText(applicationContext, "Użytkownik o podanych danych już istnieje", Toast.LENGTH_LONG).show()

                        //Created
                        201 -> {
                            Toast.makeText(applicationContext, "Konto zostało stworzone", Toast.LENGTH_LONG).show()
                            onBackPressed()
                        }
                        //inny kod odpowiedzi serwera
                        else ->{
                            Toast.makeText(applicationContext, "Błąd serwera - spróbuj później", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
    }
}
