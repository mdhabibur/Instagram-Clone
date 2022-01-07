package com.mmali.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media App!!!");

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar); //setting the toolbar as the action bar which will be displayed on the top of the activity

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager()); //needs a fragmentManager object
        viewPager.setAdapter(tabAdapter);//setting the adapter for viewPager

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false); //so that tabLayout and viewPager works smoothly together
        //because when we would click on the tabs inside the tabLayout , viewPager would need to load that corresponding fragment




    }



}