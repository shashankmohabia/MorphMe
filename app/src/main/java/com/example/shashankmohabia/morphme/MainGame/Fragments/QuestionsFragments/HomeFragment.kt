package com.example.shashankmohabia.morphme.MainGame.Fragments.QuestionsFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.shashankmohabia.morphme.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.alert
import java.util.ArrayList

class HomeFragment : Fragment() {

    var questionList: ArrayList<QuestionModel> = ArrayList()
    var questionAdapter: QuestionAdapter? = null

    var questionDb = FirebaseDatabase.getInstance().reference
    var currentPhase = "Phase1"
    var currentLevel = "Level1"

    var i = 0
    var questionCount = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getQuestionData()
        questionAdapter = QuestionAdapter(view.context, R.layout.swing_item, questionList)


        swingView?.setAdapter(questionAdapter)
        swingView?.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!")
                questionList?.removeAt(0)
                questionAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(dataObject: Any) {
                alert("Are you sure it is fake?") {
                    title = "Fake"
                    positiveButton("Yes") {}
                    negativeButton("No") { }
                }.show()
            }

            override fun onRightCardExit(dataObject: Any) {
                alert("Are you sure it is not fake?") {
                    title = " Not Fake"
                    positiveButton("Yes") {}
                    negativeButton("No") { }
                }.show()
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}

            override fun onScroll(scrollProgressPercent: Float) {}
        })


        // Optionally add an OnItemClickListener
        //swingView?.setOnItemClickListener(SwipeFlingAdapterView.OnItemClickListener { itemPosition, dataObject -> Toast.makeText(view?.context, "Left", Toast.LENGTH_LONG).show() })


    }

    private fun getQuestionData() {
        when (questionCount) {
            2 -> currentLevel = "Level2"
            4 -> currentLevel = "Level3"
            6 -> {
                currentPhase = "Phase2"
                currentLevel = "Level4"
            }
        }
        questionDb = questionDb.child("Questions").child("Phase1").child("Level1").child("Question")
        questionDb.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                if (dataSnapshot?.childrenCount?.toInt() != 0) {
                    val id = dataSnapshot?.getKey()
                    val caption = dataSnapshot?.child("caption")?.getValue()!!.toString()
                    val answer = dataSnapshot?.child("answer")?.getValue()!!.toString()
                    val companionQuestion = dataSnapshot?.child("companionQuestion")?.getValue()!!.toString()
                    val mediaDownloadUri = dataSnapshot?.child("mediaDownloadUri")?.getValue()!!.toString()
                    val item = QuestionModel(id, caption, answer, companionQuestion, mediaDownloadUri)
                    questionList.add(item)
                    questionAdapter?.notifyDataSetChanged()
                }
            }


            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {}
        })
    }

}


