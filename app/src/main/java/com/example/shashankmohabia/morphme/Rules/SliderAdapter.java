package com.example.shashankmohabia.morphme.Rules;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shashankmohabia.morphme.R;

/**
 * Created by Shashank Mohabia on 2/20/2018.
 */

public class SliderAdapter extends PagerAdapter {


    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.mipmap.soup,
            R.mipmap.owl,
            R.mipmap.boy
    };

    public String[] slider_headings = {
            "Rule 1",
            "Rule 2",
            "Rule 3"
    };


    public String[] slider_descriptions = {
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.",
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.",
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout."
    };


    @Override
    public int getCount() {
        return slider_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.rules_template, container, false);


        ImageView slideImageView = (ImageView) view.findViewById(R.id.sliderImageView);
        TextView slideHeading = (TextView) view.findViewById(R.id.sliderHeading);
        TextView slideDescription = (TextView) view.findViewById(R.id.sliderDescription);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slider_headings[position]);
        slideDescription.setText(slider_descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
