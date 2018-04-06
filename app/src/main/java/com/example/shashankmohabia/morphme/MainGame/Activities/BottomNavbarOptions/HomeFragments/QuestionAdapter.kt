package com.example.shashankmohabia.morphme.MainGame.HomeFragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.shashankmohabia.morphme.R
import kotlinx.android.synthetic.main.swing_item.view.*

/**
 * Created by Shashank Mohabia on 3/26/2018.
 */
class QuestionAdapter(context: Context, resource: Int, objects: List<QuestionModel>) : ArrayAdapter<QuestionModel>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var question = getItem(position)

        convertView = LayoutInflater.from(context).inflate(R.layout.swing_item, parent, false)

        convertView.questionTextSwing.text = question.questionCaption

        Glide.clear(convertView.questionPicSwing)
        Glide.with(convertView.context).load(question.questionMediaURL).into(convertView.questionPicSwing)
        return convertView
    }
}