package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AllUsers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shashankmohabia.morphme.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_all_users.*
import java.util.ArrayList

class AllUsersFragment : Fragment() {

    private val allUsersObjectArrayList = ArrayList<AllUsersObject>()
    private var mAdapter: AllUsersAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserDb()
        mAdapter = AllUsersAdapter(getUserObjectList(), view.context)

        allUsersRecycler.isNestedScrollingEnabled = false
        allUsersRecycler.setHasFixedSize(true)
        allUsersRecycler.layoutManager = LinearLayoutManager(view.context)
        allUsersRecycler.adapter = mAdapter

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
                    if (dataSnapshot.child("profileImageDownloadUri").value != null) {
                        profilePicUrl = dataSnapshot.child("profileImageDownloadUri").value!!.toString()
                    }


                    val obj = AllUsersObject(userId, name, profilePicUrl)
                    allUsersObjectArrayList.add(obj)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
