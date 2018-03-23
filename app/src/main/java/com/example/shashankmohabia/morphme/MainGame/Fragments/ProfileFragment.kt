package com.example.shashankmohabia.morphme.MainGame.Fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {


    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mUserDb: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var userId: String? = null
    private var resultURI: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = mAuth.currentUser!!.uid
        mUserDb = mUserDb.child("Users").child(userId)

        getUserInfo()
    }
    private fun getUserInfo() {

        //mUserDb.addValueEventListener(ob)
        mUserDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    val map = dataSnapshot.value as Map<String, Any>?
                    if (map!!["name"] != null) {
                        name.text = map["name"].toString()
                    }
                    if (map["age"] != null) {
                        age.text = map["age"].toString()
                    }
                    if (map["country"] != null) {
                        country.text = map["country"].toString()
                    }
                    if (map["gender"] != null) {
                        gender.text = map["gender"].toString()
                    }

                    email.text = mAuth.currentUser!!.email

                    Glide.clear(profilePic)
                    if (map["gender"] != null) {
                        var gender = map["gender"].toString()
                        when (gender) {
                            "male" -> Glide.with(this@ProfileFragment).load(R.mipmap.user_male).into(profilePic)
                            else -> Glide.with(this@ProfileFragment).load(R.mipmap.user_female).into(profilePic)
                        }

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
