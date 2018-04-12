@file:Suppress("DEPRECATION")

package com.example.shashankmohabia.morphme.MainGame.AdminFragments.AllUsers

import android.app.ProgressDialog
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shashankmohabia.morphme.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_all_users.*
import java.util.ArrayList
import android.support.v7.widget.DividerItemDecoration


class AllUsersFragment : Fragment() {

    private val allUsersObjectArrayList = ArrayList<AllUsersObject>()
    private var mAdapter: AllUsersAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()
        getUserDb()
        mAdapter = AllUsersAdapter(getUserObjectList(), view.context)

        allUsersRecycler.isNestedScrollingEnabled = false
        allUsersRecycler.setHasFixedSize(true)
        allUsersRecycler.layoutManager = LinearLayoutManager(view.context)
        allUsersRecycler.adapter = mAdapter
        val itemDecor = DividerItemDecoration(view.context, HORIZONTAL)
        allUsersRecycler.addItemDecoration(itemDecor)

    }

    private fun showProgressDialog() {
        val progress = ProgressDialog(view?.context)
        progress.setTitle("Please Wait")
        progress.setMessage("Loading...")
        progress.show()

        val progressRunnable = Runnable { progress.cancel() }

        val pdCanceller = Handler()
        pdCanceller.postDelayed(progressRunnable, 1500)
    }

    private fun getUserObjectList(): List<AllUsersObject> {
        return allUsersObjectArrayList
    }

    private fun getUserDb() {
        val userDb = FirebaseDatabase.getInstance().reference.child("Users")
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        fetchUserInfo(user.key)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun fetchUserInfo(key: String?) {
        val userId: String? = key
        val userDb = FirebaseDatabase.getInstance().reference.child("Users").child(key)
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var name = ""
                    var profilePicUrl = ""
                    if (dataSnapshot.child("name").value != null) {
                        name = dataSnapshot.child("name").value!!.toString()
                    }
                    if (dataSnapshot.child("profilePicUrl").value != null) {
                        profilePicUrl = dataSnapshot.child("profilePicUrl").value!!.toString()
                    }

                    var phase1Score = dataSnapshot.child("scorePhase1").value as Long
                    var phase2Score = dataSnapshot.child("scorePhase2").value as Long

                    val obj = AllUsersObject(userId, name, profilePicUrl, phase1Score + phase2Score)
                    allUsersObjectArrayList.add(obj)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
