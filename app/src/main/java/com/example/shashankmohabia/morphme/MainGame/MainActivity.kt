package com.example.shashankmohabia.morphme.MainGame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.shashankmohabia.morphme.Authentication.LoginActivity
import com.example.shashankmohabia.morphme.MainGame.Fragments.DashBoardFragment
import com.example.shashankmohabia.morphme.MainGame.Fragments.HomeFragment
import com.example.shashankmohabia.morphme.MainGame.Fragments.NotificationsFragment
import com.example.shashankmohabia.morphme.MainGame.Fragments.ProfileFragment
import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                R.id.notifications -> {
                    startFragmentTransaction(NotificationsFragment())
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

    private fun startFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, fragment)
                .commit()
    }

}
