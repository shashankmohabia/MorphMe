package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.shashankmohabia.morphme.R
import kotlinx.android.synthetic.main.all_users_item.view.*

/**
 * Created by Shashank Mohabia on 3/20/2018.
 */

class AllUsersAdapter(private val allUsersObjectList: List<AllUsersObject>, private val context: Context) : RecyclerView.Adapter<AllUsersViewHolder>() {
    override fun onBindViewHolder(holder: AllUsersViewHolder, position: Int) {
        holder.itemView.userId.text = allUsersObjectList[position].userID
        holder.itemView.userName.text = allUsersObjectList[position].userName
        when (allUsersObjectList[position].profilePicUrl) {
            "default" -> Glide.with(context).load(R.mipmap.user_male).into(holder.itemView.userPic)
            else -> Glide.with(context).load(allUsersObjectList[position].profilePicUrl).into(holder.itemView.userPic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUsersViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.all_users_item, parent, false)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.setLayoutParams(lp)
        return AllUsersViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return this.allUsersObjectList.size
    }
}