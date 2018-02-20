package com.example.shashankmohabia.morphme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewParent;
import android.widget.LinearLayout;

/**
 * Created by Shashank Mohabia on 2/20/2018.
 */

public class RulesActivity extends AppCompatActivity {

    private ViewPager mSlidePager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        mSlidePager = (ViewPager) findViewById(R.id.sliderMain);
        mDotLayout = (LinearLayout) findViewById(R.id.sliderDots);
    }

}
