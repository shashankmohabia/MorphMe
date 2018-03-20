package com.example.shashankmohabia.morphme.MainGame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.shashankmohabia.morphme.Authentication.LoginActivity
import com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AdminFragment
import com.example.shashankmohabia.morphme.MainGame.Fragments.DashBoardFragment
import com.example.shashankmohabia.morphme.MainGame.Fragments.HomeFragment
import com.example.shashankmohabia.morphme.MainGame.Fragments.ProfileFragment
import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert


class MainActivity : AppCompatActivity() {

    private var isAdmin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAdminStatus()

        setBottomNav()

    }

    private fun setBottomNav() {
        if (isAdmin.equals("no")) {
            bottonNavBar.inflateMenu(R.menu.bottom_navigation_normal_user)
            bottonNavBar.selectedItemId = R.id.home
            startFragmentTransaction(HomeFragment())

            bottonNavBar.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.profile -> {
                        startFragmentTransaction(ProfileFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.dashboard -> {
                        startFragmentTransaction(DashBoardFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.home -> {
                        startFragmentTransaction(HomeFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.logout -> {
                        alert("Do you really want to logout?") {
                            title = "Logout"
                            positiveButton("Yes") {
                                FirebaseAuth.getInstance().signOut()
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            negativeButton("No") { }
                        }.show()
                    }
                }
                return@setOnNavigationItemSelectedListener false
            }
        } else {
            bottonNavBar.inflateMenu(R.menu.bottom_navigation_admin)
            bottonNavBar.selectedItemId = R.id.home
            startFragmentTransaction(HomeFragment())

            bottonNavBar.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.profile -> {
                        startFragmentTransaction(ProfileFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.dashboard -> {
                        startFragmentTransaction(DashBoardFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.home -> {
                        startFragmentTransaction(HomeFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.adminMode -> {
                        startFragmentTransaction(AdminFragment())
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.logout -> {
                        alert("Do you really want to logout?") {
                            title = "Logout"
                            positiveButton("Yes") {
                                FirebaseAuth.getInstance().signOut()
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            negativeButton("No") { }
                        }.show()
                    }
                }
                return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun checkAdminStatus() {
        var userId: String = FirebaseAuth.getInstance().uid.toString()
        val mUserDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        mUserDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    isAdmin = dataSnapshot.child("superUserStatus").value.toString()
                    testButton.text = isAdmin

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun startFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, fragment)
                .commit()
    }

}
