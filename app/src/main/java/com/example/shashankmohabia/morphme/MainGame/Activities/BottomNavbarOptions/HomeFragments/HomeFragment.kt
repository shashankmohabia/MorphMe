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

    var userDb = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid)

    var levelTracker = 0
    var questionPerLevel = 2
    var currentLevel = "Level1"
    var phase1Score: Long = 0
    var phase2Score: Long = 0
    var currentLevelScore: Long = 0
    var levelPassed = "Level 1"
    var presentLevel = "Level1"
    val scoreArray = intArrayOf(10, 10, 20, 20, 30, 30, 40, 40)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentUserLevelAndScore()
        getQuestionData()
        questionAdapter = QuestionAdapter(view.context, R.layout.swing_item, finalQuestionList)
        setSwingCards()
    }

    private fun getCurrentUserLevelAndScore() {
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.exists()) {
                        currentLevel = dataSnapshot.child("currentLevel").value.toString()
                        presentLevel = currentLevel
                        phase1Score = dataSnapshot.child("scorePhase1").value as Long
                        phase2Score = dataSnapshot.child("scorePhase2").value as Long

                    }
                }
            }
        })
    }

    private fun getUserQuestionData() {
        //Log.d("final", currentLevel)
        updateLevelTracker()
        setLevelIndicator()
        /* Log.d("final", phase1Score.toString())
         Log.d("final", phase2Score.toString())*/
        finalQuestionList.clear()
        var i = 0
        for (q in allQuestionList) {
            if (q.questionLevel.equals(currentLevel) && i < questionPerLevel) {
                finalQuestionList.add(q)
                questionAdapter?.notifyDataSetChanged()
                i++
                when (finalQuestionList.size) {
                    questionPerLevel - levelTracker -> {
                        currentLevel = "Level2"; i = 0
                    }
                    2 * questionPerLevel - levelTracker -> {
                        currentLevel = "Level3"; i = 0
                    }
                    3 * questionPerLevel - levelTracker -> {
                        currentLevel = "Level4"; i = 0
                    }
                }
            }

            //Log.d("final", "Size: "+finalQuestionList.size.toString()+" i"+i)
        }


        //Log.d("final", finalQuestionList.size.toString())
    }

    private fun updateLevelTracker() {
        when (currentLevel) {
            "Level1" -> {
                levelTracker = 0
            }
            "Level2" -> {
                levelTracker = questionPerLevel
            }
            "Level3" -> {
                levelTracker = questionPerLevel * 2
            }
            "Level4" -> {
                levelTracker = questionPerLevel * 3
            }
        }

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
        //Log.d("final", levelTracker.toString())

        when (levelTracker) {
            questionPerLevel -> {
                level1.visibility = View.VISIBLE
                levelPassed = "Level 1"
            }
            questionPerLevel * 2 -> {
                level1.visibility = View.VISIBLE
                level2.visibility = View.VISIBLE
                levelPassed = "Level 2"
            }
            questionPerLevel * 3 -> {
                level1.visibility = View.VISIBLE
                level2.visibility = View.VISIBLE
                level3.visibility = View.VISIBLE
                levelPassed = "Level 3"
            }
            questionPerLevel * 4 -> {
                level1.visibility = View.VISIBLE
                level2.visibility = View.VISIBLE
                level3.visibility = View.VISIBLE
                level4.visibility = View.VISIBLE
                levelPassed = "Level 4"
            }
        }
    }

    private fun updateUserLevel() {
        val userLevelInfo: MutableMap<String, Any> = mutableMapOf()
        when (levelTracker) {
            questionPerLevel -> {
                currentLevelScore = 0
                userLevelInfo.put("currentLevel", "Level2")
                presentLevel = "Level2"
            }
            questionPerLevel * 2 -> {
                currentLevelScore = 0
                userLevelInfo.put("currentLevel", "Level3")
                presentLevel = "Level3"
            }
            questionPerLevel * 3 -> {
                currentLevelScore = 0
                userLevelInfo.put("currentLevel", "Level4")
                presentLevel = "Level4"
            }
            questionPerLevel * 4 -> {
                currentLevelScore = 0
                userLevelInfo.put("currentLevel", "done")
            }
        }
        userDb.updateChildren(userLevelInfo)
    }

    private fun addWrongResponse(item: QuestionModel, companionQuestionAnswer: String) {
        val dbRefer = userDb.child("Responses").child("Wrong").child(item.questionId)
        val questionInfo: MutableMap<String, Any> = mutableMapOf()
        questionInfo.put("companionQuestionResponse", companionQuestionAnswer)
        dbRefer.updateChildren(questionInfo)
        levelCompletionMessage()

        updateUserLevel()
    }

    private fun addCorrectResponse(item: QuestionModel, companionQuestionAnswer: String) {
        updateScore()
        val dbRefer = userDb.child("Responses").child("Correct").child(item.questionId)
        val questionInfo: MutableMap<String, Any> = mutableMapOf()
        questionInfo.put("companionQuestionResponse", companionQuestionAnswer)
        dbRefer.updateChildren(questionInfo)
        levelCompletionMessage()

        updateUserLevel()
    }

    private fun levelCompletionMessage() {
        if (levelTracker % questionPerLevel == 0) {
            alert {
                message = "You have successfully completed " + levelPassed
                title = "Congratulations"
            }.show()
        }
    }

    private fun updateScore() {
        val scoreInfo: MutableMap<String, Any> = mutableMapOf()
        when (presentLevel) {
            "Level1" -> {
                currentLevelScore += 10
                scoreInfo.put("scoreLevel1", currentLevelScore)
            }
            "Level2" -> {
                currentLevelScore += 20
                scoreInfo.put("scoreLevel2", currentLevelScore)
            }
            "Level3" -> {
                currentLevelScore += 30
                scoreInfo.put("scoreLevel3", currentLevelScore)
            }
            "Level4" -> {
                currentLevelScore += 40
                scoreInfo.put("scoreLevel4", currentLevelScore)
            }
        }
        //Log.d("final", levelTracker.toString())
        if (levelTracker > 6) {
            //Log.d("final", scoreArray[levelTracker-1].toString())
            phase2Score += scoreArray[levelTracker - 1]
        } else {
            //Log.d("final", scoreArray[levelTracker-1].toString())
            phase1Score += scoreArray[levelTracker - 1]
        }

        scoreInfo.put("scorePhase1", phase1Score)
        scoreInfo.put("scorePhase2", phase2Score)
        userDb.updateChildren(scoreInfo)

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

                //Log.d("final", allQuestionList.size.toString())

                Collections.sort(allQuestionList, getCompByLevel())

                /*for (q in allQuestionList) {
                    Log.d("final", "" + q.questionLevel)
                }*/

                getUserQuestionData()

                for (q in finalQuestionList) {
                    Log.d("final", "" + q.questionLevel)
                }


            }

            override fun onCancelled(p0: DatabaseError?) {}
        })


    }

    fun getCompByLevel(): Comparator<QuestionModel> {
        return Comparator { s1, s2 -> s1.questionLevel.compareTo(s2.questionLevel) }
    }
}