package com.example.shashankmohabia.morphme.MainGame.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.example.shashankmohabia.morphme.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap

class ProfileFragment : Fragment() {


    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mUserDb: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var userId: String? = null
    private var resultURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        userId = mAuth.currentUser!!.uid
        mUserDb = mUserDb.child("Users").child(userId)

        getUserInfo()
        return inflater.inflate(R.layout.fragment_profile, container, false)
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

                    email.text = mAuth.currentUser!!.email

                    Glide.clear(profilePic)
                    if (map["profileImageDownloadUri"] != null) {
                        var imageUrl = map["profileImageDownloadUri"].toString()
                        when (imageUrl) {
                            "default" -> Glide.with(this@ProfileFragment).load(R.mipmap.user).into(profilePic)
                            else -> Glide.with(this@ProfileFragment).load(imageUrl).into(profilePic)
                        }

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
