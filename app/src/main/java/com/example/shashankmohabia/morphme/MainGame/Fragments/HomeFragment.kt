package com.example.shashankmohabia.morphme.MainGame.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.shashankmohabia.morphme.R
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.alert
import java.util.ArrayList

class HomeFragment : Fragment() {

    private var al: ArrayList<String>? = null
    private var arrayAdapter: ArrayAdapter<String>? = null
    private var i: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        al = ArrayList()
        al?.add("php")
        al?.add("c")
        al?.add("python")
        al?.add("java")
        al?.add("html")
        al?.add("c++")
        al?.add("css")
        al?.add("javascript")

        arrayAdapter = ArrayAdapter(view.context, R.layout.swing_item, R.id.questionText, al)


        swingView?.setAdapter(arrayAdapter)
        swingView?.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!")
                al?.removeAt(0)
                arrayAdapter?.notifyDataSetChanged()
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

}


