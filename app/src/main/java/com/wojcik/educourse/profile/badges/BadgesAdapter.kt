package com.wojcik.educourse.profile.badges

import android.content.Context
import android.provider.Settings.Global.getString
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.wojcik.educourse.R
import com.wojcik.educourse.profile.badges.data.UserBadgesResponse
import kotlinx.android.synthetic.main.item_badge.view.*
import java.lang.StringBuilder

class BadgesAdapter(val context: Context, private val badges: List<UserBadgesResponse>) : RecyclerView.Adapter<BadgesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_badge, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return badges.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val badge = badges[position]
        holder.setData(badge, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(badge: UserBadgesResponse?, pos: Int) {
            Picasso.get().load(badge!!.image).into(itemView.badge_image)
            itemView.titleView.text = (badge.name)
            itemView.descriptionView.text = (badge.description)
            val points = badge.experiencePoints
            val stringBuilder = StringBuilder()
            stringBuilder.append("Punkty $points")
            itemView.pointsView.text = stringBuilder.toString()
        }
    }


}