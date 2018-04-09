package com.example.shashankmohabia.morphme.MainGame.Activities.BottomNavbarOptions.HomeFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shashankmohabia.morphme.MainGame.HomeFragments.QuestionAdapter
import com.example.shashankmohabia.morphme.MainGame.HomeFragments.QuestionModel
import com.example.shashankmohabia.morphme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.alert
import java.util.*

class HomeFragment : Fragment() {

    var allQuestionList: ArrayList<QuestionModel> = ArrayList()
    var finalQuestionList: ArrayList<QuestionModel> = ArrayList()
    var questionAdapter: QuestionAdapter? = null

    var levelTracker = 0
    var questionPerLevel = 2


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getQuestionData()
        questionAdapter = QuestionAdapter(view.context, R.layout.swing_item, finalQuestionList)

        setSwingCards()
    }

    private fun getUserQuestionData() {

        finalQuestionList.clear()

        var level = "Level1"
        var i = 0
        for (q in allQuestionList) {
            if (q.questionLevel.equals(level) && i < questionPerLevel) {
                finalQuestionList.add(q)
                questionAdapter?.notifyDataSetChanged()
                i++
                when (finalQuestionList.size) {
                    questionPerLevel -> {level = "Level2"; i=0}
                    2*questionPerLevel -> {level = "Level3"; i=0}
                    3*questionPerLevel -> {level = "Level4"; i=0}
                }
            }

            //Log.d("final", "Size: "+finalQuestionList.size.toString()+" i"+i)
        }

        /*var EXITCODE: Boolean = true
        while (questionCount < 8) {
            //Log.d("final", questionCount.toString())
            for (question in allQuestionList) {
                //Log.d("final", " second "+questionCount.toString())
                Log.d("final", "Phase: " + question.questionPhase + " Level: " + question.questionLevel)
                Log.d("final", "CPhase: " + currentPhase + " CLevel: " + currentLevel)
                if (question.questionPhase.equals(currentPhase) and question.questionLevel.equals(currentLevel) and EXITCODE) {
                    finalQuestionList.add(question)
                    questionCount++
                    //Log.d("final", currentPhase + " " + currentLevel)
                    when (questionCount) {
                        2 -> currentLevel = "Level2"
                        4 -> currentLevel = "Level3"
                        6 -> {
                            currentLevel = "Level4"
                            currentPhase = "Phase2"
                        }
                        8 -> EXITCODE = false
                    }
                    Log.d("final", currentPhase + " " + currentLevel)
                    continue
                } else {
                    break
                }
            }
        }*/

        //Log.d("final", finalQuestionList.size.toString())
    }

    private fun setSwingCards() {
        swingView?.setAdapter(questionAdapter)
        swingView?.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!")
                finalQuestionList.removeAt(0)
                questionAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(dataObject: Any) {
                val item = dataObject as QuestionModel
                alert(item.questionCompanionQuestion.toString()) {
                    title = "Fake"
                    positiveButton("Yes") {
                        val companionQuestionAnswer = "Yes"
                        if (item.questionAnswer.equals("Fake")) {
                            addCorrectResponse(item, companionQuestionAnswer)
                        } else {
                            addWrongResponse(item, companionQuestionAnswer)
                        }
                    }
                    negativeButton("No") {
                        val companionQuestionAnswer = "No"
                        if (item.questionAnswer.equals("Fake")) {
                            addCorrectResponse(item, companionQuestionAnswer)
                        } else {
                            addWrongResponse(item, companionQuestionAnswer)
                        }
                    }
                }.show()

                levelTracker++
                setLevelIndicator()

            }

            override fun onRightCardExit(dataObject: Any) {
                val item = dataObject as QuestionModel
                alert(item.questionCompanionQuestion.toString()) {
                    title = " Not Fake"
                    positiveButton("Yes") {
                        val companionQuestionAnswer = "Yes"
                        if (item.questionAnswer.equals("Not Fake")) {
                            addCorrectResponse(item, companionQuestionAnswer)
                        } else {
                            addWrongResponse(item, companionQuestionAnswer)
                        }
                    }
                    negativeButton("No") {
                        val companionQuestionAnswer = "No"
                        if (item.questionAnswer.equals("Not Fake")) {
                            addCorrectResponse(item, companionQuestionAnswer)
                        } else {
                            addWrongResponse(item, companionQuestionAnswer)
                        }
                    }
                }.show()

                levelTracker++
                setLevelIndicator()
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}

            override fun onScroll(scrollProgressPercent: Float) {}
        })


        // Optionally add an OnItemClickListener
        //swingView?.setOnItemClickListener(SwipeFlingAdapterView.OnItemClickListener { itemPosition, dataObject -> Toast.makeText(view?.context, "Left", Toast.LENGTH_LONG).show() })


    }

    private fun setLevelIndicator() {
        when (levelTracker) {
            2 -> {
                level1.visibility = View.VISIBLE
            }
            4 -> {
                level2.visibility = View.VISIBLE
            }
            6 -> {
                level3.visibility = View.VISIBLE
            }
            8 -> {
                level4.visibility = View.VISIBLE
            }
        }
    }

    private fun addWrongResponse(item: QuestionModel, companionQuestionAnswer: String) {
        val dbRefer = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid).child("Responses").child("Wrong").child(item.questionId)
        val questionInfo: MutableMap<String, Any> = mutableMapOf()
        questionInfo.put("companionQuestionResponse", companionQuestionAnswer)
        dbRefer.updateChildren(questionInfo)
    }

    private fun addCorrectResponse(item: QuestionModel, companionQuestionAnswer: String) {
        val dbRefer = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid).child("Responses").child("Correct").child(item.questionId)
        val questionInfo: MutableMap<String, Any> = mutableMapOf()
        questionInfo.put("companionQuestionResponse", companionQuestionAnswer)
        dbRefer.updateChildren(questionInfo)
    }

    private fun getQuestionData() {

        val questionDb = FirebaseDatabase.getInstance().reference.child("Questions")
        questionDb.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.exists()) {
                        Log.d("allizom", dataSnapshot.value.toString())
                        for (question in dataSnapshot.children) {
                            Log.d("allizom", question.child("caption").value.toString())
                            allQuestionList.add(
                                    QuestionModel(
                                            questionId = question.key,
                                            questionCaption = question.child("caption").value.toString(),
                                            questionAnswer = question.child("answer").value.toString(),
                                            questionCompanionQuestion = question.child("companionQuestion").value.toString(),
                                            questionMediaURL = question.child("mediaDownloadUri").value.toString(),
                                            questionPhase = question.child("phase").value.toString(),
                                            questionLevel = question.child("level").value.toString()
                                    )
                            )

                        }
                    }
                }

                Log.d("final", allQuestionList.size.toString())

                Collections.sort(allQuestionList, getCompByLevel())

                for (q in allQuestionList) {
                    Log.d("final", "" + q.questionLevel)
                }


                getUserQuestionData()

                for (q in finalQuestionList) {
                    Log.d("final", "" + q.questionLevel)
                }





            }

            override fun onCancelled(p0: DatabaseError?) {}
        })


    }

    fun getCompByLevel(): Comparator<QuestionModel> {
        return object : Comparator<QuestionModel> {
            override fun compare(s1: QuestionModel, s2: QuestionModel): Int {
                return s1.questionLevel.compareTo(s2.questionLevel)
            }
        }
    }

}


