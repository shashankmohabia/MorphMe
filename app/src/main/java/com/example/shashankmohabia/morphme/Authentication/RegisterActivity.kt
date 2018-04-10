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

    var gender: String? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseAuthStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        getGender()

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
                        userInfo.put("superUserStatus", "no")
                        userInfo.put("gender", gender.toString())
                        userInfo.put("profilePicUrl", "default")
                        userInfo.put("currentLevel", "Level1")
                        userInfo.put("scorePhase1", 0)
                        userInfo.put("scorePhase2", 0)
                        dbRefer.updateChildren(userInfo)
                    }
                })
            } else {
                textPasswordDoNotMatch.visibility = View.VISIBLE
            }
        }
    }

    private fun getGender() {
        val genders = arrayOf("Male", "Female", "Other")
        selectGender.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, genders)
        selectGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (selectGender.selectedItem.toString()) {
                    "Male" -> gender = "Male"
                    "Female" -> gender = "Female"
                    "Other" -> gender = "Other"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
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
