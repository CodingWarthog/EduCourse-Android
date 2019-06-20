package com.wojcik.educourse.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.profile.data.UserResponse
import kotlinx.android.synthetic.main.fragment_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoFragment : Fragment() {

    var username: String = ""

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_info, null)
        this.getUserInfo(rootView)
        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getString("username")?.let {
            username = it
        }
    }

    private fun getUserInfo(rootView: View) {
        val args = arguments

        RetrofitClient.instance.getUserByUsername(args!!.getString("username")!!)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(context, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                    progressBarProfile.visibility = View.INVISIBLE
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    val builder = StringBuilder()
                    builder.append(response.body()!!.firstname).append(" ").append(response.body()!!.lastname)
                    val textName = rootView.findViewById<TextView>(R.id.text_name_value)
                    textName.text = builder.toString()
                    val textUsername = rootView.findViewById<TextView>(R.id.text_username_value)
                    textUsername.text = response.body()!!.username
                    val textEmail = rootView.findViewById<TextView>(R.id.text_email_value)
                    textEmail.text = response.body()!!.email
                    val textRole = rootView.findViewById<TextView>(R.id.text_roles_value)
                    textRole.text = response.body()!!.role
                    progressBarProfile.visibility = View.INVISIBLE
                }

            })
    }

}