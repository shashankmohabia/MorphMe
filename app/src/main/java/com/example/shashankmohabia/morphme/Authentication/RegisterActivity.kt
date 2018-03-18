package com.example.shashankmohabia.morphme.Authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.shashankmohabia.morphme.R
import com.example.shashankmohabia.morphme.Rules.RulesActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var firebaseAuthStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val intent = Intent(this, RulesActivity::class.java)
                startActivity(intent)
                finish()
                return@AuthStateListener
            }
        }

        cardRegister!!.setOnClickListener {
            if (editPassword.text.toString() == editConfirmPassword.text.toString()) {
                mAuth!!.createUserWithEmailAndPassword(editEmailID.text.toString(), editPassword.text.toString()).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Sign Up Error", Toast.LENGTH_SHORT).show()
                    } else {
                        val userId = mAuth!!.currentUser!!.uid
                        val dbRefer = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
                        var userInfo: MutableMap<String, Any> = mutableMapOf()
                        userInfo.put("name", editName.text.toString())
                        userInfo.put("age", editAge.text.toString())
                        userInfo.put("country", editCountry.text.toString())
                        userInfo.put("profileImageDownloadUri", "default")
                        dbRefer.updateChildren(userInfo)

                        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                textPasswordDoNotMatch.visibility = View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(firebaseAuthStateListener!!)
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(firebaseAuthStateListener!!)
    }
}
