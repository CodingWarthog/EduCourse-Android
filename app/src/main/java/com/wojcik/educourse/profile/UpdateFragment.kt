package com.wojcik.educourse.profile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.profile.data.UserResponse
import kotlinx.android.synthetic.main.fragment_update.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateFragment : Fragment() {

    private var userId = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_update, container, false)

        this.getUserInfo()

        val button = myView.findViewById<Button>(R.id.confirmButton)
        button.setOnClickListener {
            editData(myView)
        }


        return myView
    }

    private fun editData(view: View) {
        if(nameTxt.text.toString().isEmpty()){
            nameTxt.error = "Wpisz imię"
            nameTxt.requestFocus()
            return
        }
        if(surnameTxt.text.toString().isEmpty()){
            surnameTxt.error = "Wpisz login"
            surnameTxt.requestFocus()
            return
        }

        val completeDialog = AlertDialog.Builder(view.context)
        completeDialog.setMessage("Czy na pewno chcesz zaktualizować dane?")
        completeDialog.setCancelable(true)
        completeDialog.setPositiveButton("Tak") { _: DialogInterface, _: Int ->

            val userResponse = UserResponse(userId, nameTxt.text.toString(), surnameTxt.text.toString(), usernameTxt.text.toString(), null, emailTxt.text.toString())

            RetrofitClient.instance.updateUser(userId, userResponse)
                .enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(context, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        Toast.makeText(context, "Dane zauktualizowane", Toast.LENGTH_SHORT).show()
                    }

                })
        }
        completeDialog.setNegativeButton("Nie") { _: DialogInterface, _: Int ->

        }
        completeDialog.create().show()
    }

    private fun getUserInfo() {
        val args = arguments

        RetrofitClient.instance.getUserByUsername(args!!.getString("username")!!)
            .enqueue(object: Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(context, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    userId = response.body()!!.id
                    nameTxt.setText(response.body()!!.firstname)
                    surnameTxt.setText(response.body()!!.lastname)
                    usernameTxt.setText(response.body()!!.username)
                    emailTxt.setText(response.body()!!.email)
                }
            })
    }
}