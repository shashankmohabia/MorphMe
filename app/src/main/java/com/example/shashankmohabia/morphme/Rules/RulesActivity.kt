package com.example.shashankmohabia.morphme.Rules

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.shashankmohabia.morphme.MainGame.MainActivity
import com.example.shashankmohabia.morphme.R
import kotlinx.android.synthetic.main.activity_rules.*

/**
 * Created by Shashank Mohabia on 2/20/2018.
 */

class RulesActivity : AppCompatActivity() {

    var count: Int = 100
    val mDots = arrayOfNulls<TextView>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val sliderAdapter = SliderAdapter(this)
        sliderMain!!.adapter = sliderAdapter
        addDotsIndicator(0)
        sliderMain.addOnPageChangeListener(viewListener)

        nextButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    fun addDotsIndicator(position: Int) {

        sliderDots.removeAllViews()

        for (i in mDots.indices) {
            mDots[i] = TextView(this)
            mDots[i]!!.text = Html.fromHtml("&#8226;")
            mDots[i]!!.textSize = 35f
            mDots[i]!!.setTextColor(resources.getColor(R.color.black_overlay))
            sliderDots.addView(mDots[i])
        }
        if (mDots.size > 0) {
            mDots[position]!!.setTextColor(resources.getColor(R.color.white))
        }
    }

    internal var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            addDotsIndicator(position)
            count = position

            if (position == mDots.size-1) {
                nextButton.isEnabled = true

                nextButton.visibility = Button.VISIBLE
                nextButton.text = "Let's Go"

            } else {
                nextButton.isEnabled = false

                nextButton.visibility = Button.INVISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }


}
