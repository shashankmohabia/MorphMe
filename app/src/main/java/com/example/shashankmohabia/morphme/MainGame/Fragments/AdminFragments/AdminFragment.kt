package com.example.shashankmohabia.morphme.MainGame.Fragments.AdminFragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shashankmohabia.morphme.R
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allUsers.setOnClickListener(View.OnClickListener {
            val fragment2 = AllUsersFragment()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.mainFrame, fragment2)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        })
    }
}
