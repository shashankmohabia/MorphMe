package com.example.shashankmohabia.morphme.MainGame.Activities.BottomNavbarOptions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashBoardFragment : Fragment() {

    var phase1Score: Long = 0
    var phase2Score: Long = 0
    var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        userID = FirebaseAuth.getInstance().currentUser?.uid
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateScore()

    }

    private fun showScore() {
        Phase1Score.text = phase1Score.toString()
        Phase2Score.text = phase2Score.toString()
    }

    private fun calculateScore() {
        val userDb = FirebaseDatabase.getInstance().reference.child("Users").child(userID)
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.exists()) {
                        phase1Score = dataSnapshot.child("scorePhase1").value as Long
                        phase2Score = dataSnapshot.child("scorePhase2").value as Long
                       // Log.d("dash", phase1Score.toString())
                        showScore()
                    }
                }
            }
        })

    }
}
