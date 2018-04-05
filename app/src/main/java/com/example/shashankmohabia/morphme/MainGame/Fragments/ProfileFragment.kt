package com.example.shashankmohabia.morphme.MainGame.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.shashankmohabia.morphme.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_question.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream
import java.io.IOException

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
        profilePic.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri = data.data
            resultURI = imageUri
            profilePic.setImageURI(resultURI)
            updateProfilePic()
        }
    }

    private fun updateProfilePic() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val userDb = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId)

        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.exists()!!) {
                    if (resultURI != null) {
                        val filepath = FirebaseStorage.getInstance().reference.child("ProfilePics").child(currentUserId.toString())
                        var bitmap: Bitmap? = null

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(view?.context?.getContentResolver(), resultURI)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        val baos = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                        val data = baos.toByteArray()
                        val ult = filepath.putBytes(data)
                        ult.addOnFailureListener {}
                        ult.addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                            val downloadUri = taskSnapshot.downloadUrl
                            val userInfo: MutableMap<String, Any> = mutableMapOf()
                            userInfo.put("profilePicUrl", downloadUri!!.toString())
                            userDb.updateChildren(userInfo)
                            return@OnSuccessListener
                        })
                    }

                    toast("Your profile pic successfully changed ")

                } else {
                    toast("Datasnapshot does not exist")
                }
            }

        })

    }


    private fun getUserInfo() {

        mUserDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    val map = dataSnapshot.value as Map<*, *>?
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
                    if (map["profilePicUrl"] != null) {
                        val profilePicUrl = map["profilePicUrl"].toString()
                        when (profilePicUrl) {
                            "default" -> Glide.with(this@ProfileFragment).load(R.drawable.ic_user).into(profilePic)
                            else -> Glide.with(this@ProfileFragment).load(profilePicUrl).into(profilePic)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
