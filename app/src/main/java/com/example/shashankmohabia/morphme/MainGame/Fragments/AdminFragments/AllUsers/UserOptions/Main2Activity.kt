package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers.UserOptions

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.shashankmohabia.morphme.Authentication.RegisterActivity
import com.example.shashankmohabia.morphme.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    var id: String? = null
    var superUserStatus:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        id = intent.getStringExtra("id")

        val userDb = FirebaseDatabase.getInstance().reference.child("Users").child(id)
        userDb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    if (p0.exists()) {
                        superUserStatus = p0.child("superUserStatus").value.toString()
                        if (superUserStatus.equals("yes")) {
                            updateAdminStatusText.text = "Remove Admin"
                        }else{
                            updateAdminStatusText.text = "Make Admin"
                        }
                    }
                }
            }

        })

        updateAdminStatus.setOnClickListener(View.OnClickListener {
            if (superUserStatus.equals("no")) {
                userDb.child("superUserStatus").setValue("yes")
            } else {
                userDb.child("superUserStatus").setValue("no")
            }
        })

    }


}
