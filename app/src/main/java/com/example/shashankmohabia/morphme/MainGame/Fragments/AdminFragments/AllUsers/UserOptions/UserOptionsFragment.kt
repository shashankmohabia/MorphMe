package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers.UserOptions

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_user_options.*

class UserOptionsFragment : Fragment() {

    val userId: String? = null
    var userDb = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userDb.child("superUserStatus").key.toString() == "Yes") {
            updateAdminStatusText.text = "Remove admin"
        }
        updateAdminStatus.setOnClickListener {
            View.OnClickListener {

            }
        }
    }


}// Required empty public constructor
