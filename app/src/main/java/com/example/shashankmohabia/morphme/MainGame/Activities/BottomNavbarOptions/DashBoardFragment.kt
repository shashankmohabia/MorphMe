package com.example.shashankmohabia.morphme.MainGame.Activities.BottomNavbarOptions

import android.graphics.Color
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
import com.github.mikephil.charting.components.Legend
import android.graphics.Typeface
import com.github.mikephil.charting.charts.PieChart
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.SpannableString
import com.example.shashankmohabia.morphme.MainGame.HomeFragments.QuestionModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.ArrayList


@Suppress("DEPRECATION")
class DashBoardFragment : Fragment() {

    var phase1Score: Long = 0
    var phase2Score: Long = 0
    var level1Score: Long = 0
    var level2Score: Long = 0
    var level3Score: Long = 0
    var level4Score: Long = 0

    var userID: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        userID = FirebaseAuth.getInstance().currentUser?.uid
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateScore()

    }

    private fun showPieChart() {
        chart1.description.isEnabled = false

        val tf = Typeface.createFromAsset(activity!!.assets, "OpenSans-Light.ttf")

        chart1.setCenterTextTypeface(tf)
        chart1.centerText = generateCenterText()
        chart1.setCenterTextSize(10f)
        chart1.setCenterTextTypeface(tf)

        // radius of the center hole in percent of maximum radius
        chart1.holeRadius = 45f
        chart1.transparentCircleRadius = 50f

        val l = chart1.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)

        chart1.data = generatePieData()

        chart1.invalidate();
    }

    private fun generatePieData(): PieData? {
        val entries: ArrayList<PieEntry> = ArrayList()

        entries.add(PieEntry(level1Score.toFloat(), "Level 1"))
        entries.add(PieEntry(level2Score.toFloat(), "Level 2"))
        entries.add(PieEntry(level3Score.toFloat(), "Level 3"))
        entries.add(PieEntry(level4Score.toFloat(), "Level 4"))

        val set = PieDataSet(entries, "")
        val MY_COLORS = intArrayOf(
                view!!.context.getResources().getColor(R.color.red),
                view!!.context.getResources().getColor(R.color.blue),
                view!!.context.getResources().getColor(R.color.green),
                view!!.context.getResources().getColor(R.color.yellow)
        )
        val colors = ArrayList<Int>()

        for (c in MY_COLORS) colors.add(c)

        set.colors = colors
        set.valueTextSize = 18f
        val data = PieData(set)
        return data
    }

    private fun generateCenterText(): SpannableString? {
        val centerText = "Score\nSummary"
        val s = SpannableString(centerText)
        s.setSpan(RelativeSizeSpan(2f), 0, centerText.length, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 0, centerText.length, 0)
        return s
    }


    private fun showScore() {
        showPieChart()
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
                        level1Score = dataSnapshot.child("scoreLevel1").value as Long
                        level2Score = dataSnapshot.child("scoreLevel2").value as Long
                        level3Score = dataSnapshot.child("scoreLevel3").value as Long
                        level4Score = dataSnapshot.child("scoreLevel4").value as Long

                        // Log.d("dash", phase1Score.toString())
                        showScore()
                    }
                }
            }
        })

    }
}
