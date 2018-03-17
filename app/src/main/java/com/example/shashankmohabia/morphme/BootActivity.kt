package com.example.shashankmohabia.morphme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Thread.sleep

class BootActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boot)

        var booticon: ImageView = findViewById(R.id.booticon)
        var myanim: Animation = AnimationUtils.loadAnimation(this, R.anim.transitions)
        booticon.startAnimation(myanim)
        val intent = Intent(this, LoginActivity::class.java)
        Thread {
            try {
                sleep(3000);
            } catch (e: InterruptedException) {
                e.printStackTrace();
            } finally {
                startActivity(intent)
                finish()
            }

        }.start()
    }
}
