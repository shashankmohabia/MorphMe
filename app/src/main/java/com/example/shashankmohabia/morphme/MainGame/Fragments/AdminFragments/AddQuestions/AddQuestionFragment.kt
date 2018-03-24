package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AddQuestions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.example.shashankmohabia.morphme.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_question.*
import org.jetbrains.anko.support.v4.toast


class AddQuestionFragment : Fragment() {

    var phase: String? = null
    var level: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPhaseAndLevel()
        var caption = questionCaption.text.toString()
        cardAddQuestion.setOnClickListener(View.OnClickListener {
            if (phase.equals("Phase1") and level.equals("Level4")) {
                questionAdditionError.text = "You can add only Level1, 2, 3 questions for Phase1"
                questionAdditionError.visibility = View.VISIBLE
            } else if (phase.equals("Phase2") and (level != "Level4")) {
                questionAdditionError.text = "You can add only Level4 questions for Phase2"
                questionAdditionError.visibility = View.VISIBLE
            } else {
                var questionDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Questions").child(phase).child(level)
                questionDb.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val key = questionDb.child("Question").push().key
                            questionDb = questionDb.child("Question").child(key)
                            var questionInfo: MutableMap<String, Any> = mutableMapOf()
                            questionInfo.put("caption", questionCaption.text.toString())
                            questionDb.updateChildren(questionInfo)
                            questionAdditionError.visibility = View.INVISIBLE
                            var numberOfChilds = dataSnapshot.child("Question").childrenCount + 1

                            toast("Your questionis successfully added " + numberOfChilds.toString())

                        } else {
                            toast("Datasnapshot does not exist")
                        }
                    }

                })
            }
        })

    }

    private fun getPhaseAndLevel() {
        val phases = arrayOf("Phase 1", "Phase 2")
        var levels = arrayOf("Level 1", "Level 2", "Level 3", "Level 4")
        selectPhase.adapter = ArrayAdapter(view?.context, R.layout.support_simple_spinner_dropdown_item, phases)
        selectLevel.adapter = ArrayAdapter(view?.context, R.layout.support_simple_spinner_dropdown_item, levels)

        /*set click listener*/
        selectPhase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (selectPhase.selectedItem.toString()) {
                    "Phase 1" -> phase = "Phase1"
                    "Phase 2" -> phase = "Phase2"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        selectLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (selectLevel.selectedItem.toString()) {
                    "Level 1" -> level = "Level1"
                    "Level 2" -> level = "Level2"
                    "Level 3" -> level = "Level3"
                    "Level 4" -> level = "Level4"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}




