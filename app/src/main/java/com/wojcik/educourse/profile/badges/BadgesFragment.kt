package com.wojcik.educourse.profile.badges

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.profile.badges.data.BadgeAssignmentResponse
import com.wojcik.educourse.profile.badges.data.UserBadgesResponse
import kotlinx.android.synthetic.main.fragment_badges.*
import kotlinx.android.synthetic.main.item_badge.view.*
import retrofit2.Call
import retrofit2.Response

class BadgesFragment : Fragment() {

    var userId = 0

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_badges, container, false)

        if(userId != 0) {
            RetrofitClient.instance.getBadgesById(userId)
                .enqueue(object : retrofit2.Callback<BadgeAssignmentResponse> {
                    override fun onFailure(call: Call<BadgeAssignmentResponse>, t: Throwable) {
                        Toast.makeText(context, "Błąd w połączeniu", Toast.LENGTH_LONG).show()
                        progressBarBadges.visibility = View.INVISIBLE
                    }

                    override fun onResponse(
                        call: Call<BadgeAssignmentResponse>,
                        response: Response<BadgeAssignmentResponse>
                    ) {
                        val badgeAssignment = response.body()!!.badgeAssignment

                        val badgesList = ArrayList<UserBadgesResponse>()
                        for(i in 0 until badgeAssignment.size) {
                            badgesList.add(badgeAssignment[i].badge)
                        }

                        val layoutManager = LinearLayoutManager(context)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        recyclerViewBadges.layoutManager = layoutManager

                        val adapter = BadgesAdapter(context!!, badgesList)
                        recyclerViewBadges.adapter = adapter


                        if (badgeAssignment.isEmpty()) {
                            val info = rootView.findViewById<TextView>(R.id.no_bages_info)
                            info.visibility = View.VISIBLE
                        }
                        progressBarBadges.visibility = View.INVISIBLE
                    }
                })
        }

        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getInt("userId")?.let {
            userId = it
        }
    }

}