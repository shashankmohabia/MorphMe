package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments.AddQuestions

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.example.shashankmohabia.morphme.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_question.*
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap


class AddQuestionFragment : Fragment() {

    var phase: String? = null
    var level: String? = null
    var answer: String? = null
    var resultImageURI: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPhaseAndLevel()
        getAnswer()
        questionMedia.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        })
        cardAddQuestion.setOnClickListener(View.OnClickListener {
            addQuestion()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageUri = data.data
            resultImageURI = imageUri
            questionMedia.setImageURI(resultImageURI)
        }
    }

    private fun addQuestion() {
        if (phase.equals("Phase1") and level.equals("Level4")) {
            questionAdditionError.text = "You can add only Level1, 2, 3 questions for Phase1"
            questionAdditionError.visibility = View.VISIBLE
        } else if (phase.equals("Phase2") and (level != "Level4")) {
            questionAdditionError.text = "You can add only Level4 questions for Phase2"
            questionAdditionError.visibility = View.VISIBLE
        } else {
            var questionDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Questions")
            questionDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val key = questionDb.push().key
                        questionDb = questionDb.child(key)
                        var questionInfo: MutableMap<String, Any> = mutableMapOf()
                        questionInfo.put("caption", questionCaption.text.toString())
                        questionInfo.put("answer", answer.toString())
                        questionInfo.put("phase", phase.toString())
                        questionInfo.put("level", level.toString())
                        questionInfo.put("companionQuestion", companionQuestion.text.toString())
                        questionDb.updateChildren(questionInfo)
                        questionAdditionError.visibility = View.INVISIBLE
                        var numberOfChilds = dataSnapshot.child("Question").childrenCount + 1

                        if (resultImageURI != null) {
                            val filepath = FirebaseStorage.getInstance().reference.child("QuestionMedia").child(key)
                            var bitmap: Bitmap? = null

                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(view?.context?.getContentResolver(), resultImageURI)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                            val baos = ByteArrayOutputStream()
                            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                            val data = baos.toByteArray()
                            val ult = filepath.putBytes(data)
                            ult.addOnFailureListener {}
                            ult.addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                                val downloadUri = taskSnapshot.downloadUrl
                                var questionInfo: MutableMap<String, Any> = mutableMapOf()
                                questionInfo.put("mediaDownloadUri", downloadUri!!.toString())
                                questionDb.updateChildren(questionInfo)
                                return@OnSuccessListener
                            })
                        }

                        toast("Your question is successfully added " + numberOfChilds.toString())
                        questionCaption.text = null
                        companionQuestion.text = null
                        questionMedia.setImageResource(R.drawable.ic_notifications_black_24dp)

                    } else {
                        toast("Datasnapshot does not exist")
                    }
                }

            })
        }
    }

    private fun getAnswer() {
        val answers = arrayOf("Fake", "Not Fake")
        selectAns.adapter = ArrayAdapter(view?.context, R.layout.support_simple_spinner_dropdown_item, answers)
        selectAns.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (selectAns.selectedItem.toString()) {
                    "Fake" -> answer = "Fake"
                    "Not Fake" -> answer = "Not Fake"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
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




