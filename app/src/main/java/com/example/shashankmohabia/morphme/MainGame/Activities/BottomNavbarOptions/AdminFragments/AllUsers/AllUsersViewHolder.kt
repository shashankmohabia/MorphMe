package com.example.shashankmohabia.morphme.MainGame.AdminFragments.AllUsers

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.shashankmohabia.morphme.MainGame.AdminFragments.AllUsers.UserOptions.UserOptionsActivity
import kotlinx.android.synthetic.main.all_users_item.view.*

/**
 * Created by Shashank Mohabia on 3/20/2018.
 */

class AllUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var context: Context? = null
    var id:String? = null

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        val intent = Intent(v.context, UserOptionsActivity::class.java)
        intent.putExtra("id", id)
        v.context.startActivity(intent)
        //v.context.fragmentManager?.beginTransaction()?.replace(R.id.mainFrame, AllUsersFragment())?.addToBackStack(null)?.commit()
    }
}