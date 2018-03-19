package com.example.shashankmohabia.morphme.Authentication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.shashankmohabia.morphme.MainGame.MainActivity
import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var firebaseAuthStateListener: FirebaseAuth.AuthStateListener? = null
    private var isAdmin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
            if (mAuth!!.currentUser != null) {
                checkAdminStatus()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("isAdmin", isAdmin)
                startActivity(intent)
                finish()
                return@AuthStateListener
            }
        }

        textRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })

        cardLogin.setOnClickListener(View.OnClickListener {
            mAuth!!.signInWithEmailAndPassword(loginEmailID.text.toString(), loginPassword.text.toString()).addOnCompleteListener(this@LoginActivity) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Sign In Error", Toast.LENGTH_SHORT).show()
                    loginError.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun checkAdminStatus() {
        var userId: String = FirebaseAuth.getInstance().uid.toString()
        val mUserDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        mUserDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    isAdmin = dataSnapshot.child("superUserStatus").value.toString()


                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
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
