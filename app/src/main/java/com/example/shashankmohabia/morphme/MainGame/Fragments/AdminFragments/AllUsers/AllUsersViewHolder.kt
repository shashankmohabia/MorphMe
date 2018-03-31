package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers.UserOptions.Main2Activity
import com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers.UserOptions.UserOptionsFragment
import com.example.shashankmohabia.morphme.MainGame.MainActivity
import com.example.shashankmohabia.morphme.R
import kotlinx.android.synthetic.main.all_users_item.view.*
import org.jetbrains.anko.toast

/**
 * Created by Shashank Mohabia on 3/20/2018.
 */

class AllUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var context:Context? = null

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        val intent= Intent(v.context, Main2Activity::class.java)
        intent.putExtra("id", this.itemView.userId.text)
        v.context.startActivity(intent)
        //v.context.fragmentManager?.beginTransaction()?.replace(R.id.mainFrame, AllUsersFragment())?.addToBackStack(null)?.commit()
         //supportFragmentManager.beginTransaction()
           //     .replace(R.id.mainFrame, UserOptionsFragment())
             //   .commit()
    }
}