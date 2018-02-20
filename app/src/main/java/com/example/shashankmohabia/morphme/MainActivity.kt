package com.example.shashankmohabia.morphme

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_popup.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.profile -> {
                message.setText("Profile")
                return@OnNavigationItemSelectedListener true
            }
            R.id.dashboard -> {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)

                return@OnNavigationItemSelectedListener true
            }
            R.id.logout -> {
                val mydialog = Dialog(this)
                mydialog.setContentView(R.layout.custom_popup)
                var yes = mydialog.findViewById<Button>(R.id.yesButton)

                var no = mydialog.findViewById<Button>(R.id.noButton)
                yes.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                })
                no.setOnClickListener(View.OnClickListener {
                    mydialog.dismiss()
                })
                mydialog.show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
