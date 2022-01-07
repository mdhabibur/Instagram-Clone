package com.mmali.instagramclone;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    //we need to add the tabs/fragments to the tabLayout and this is done via TabAdapter which extends FragmentViewPager abstract class

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
        //means we must call the super constructor of the FragmentViewPagerAdapter class for that we need this constructor
    }



    @NonNull
    @Override
    public Fragment getItem(int tabPosition) {
        //it returns a Fragment object

        switch(tabPosition){
            //here we can specify the position of the tabs inside the tab layout

            case 0:
                //instantiate the profile tab/fragment in the first position
                ProfileTab profileTab = new ProfileTab();
                return profileTab;

            case 1:
                UsersTab usersTab = new UsersTab();
                return usersTab;

            case 2:
                return new SharePictureTab();

            //if none of these cases are fulfilled then android will use the default case
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        //how many tabs do we have inside the tab layout
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //define the title of each tab
        switch (position){
            case 0:
                return "Profile";

            case 1:
                return "Users";

            case 2:
                return "Share Picture";

            default:
                return null;
        }
    }
}
