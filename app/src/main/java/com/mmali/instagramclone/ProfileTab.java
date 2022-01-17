package com.mmali.instagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class ProfileTab extends Fragment {


    private EditText edtProfileName, edtProfileBio, edtProfileProfession, edtProfileHobbies, edtProfileFavSport;
    private Button btnUpdateInfo;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        //this view holds every view so we can reference to any other views using that parent view
        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileFavSport = view.findViewById(R.id.edtProfileFavouriteSport);

        btnUpdateInfo = view.findViewById(R.id.btnProfileUpdateInfo);

        //give current user option to set his profile info and update info also
        //for that we will need a reference to the current user who is logged in to our app

        ParseUser parseUser = ParseUser.getCurrentUser(); //got reference to the current user of the app

        //set the updated profile into for the current user

//        edtProfileName.setText(parseUser.get("profileName").toString());

        //parseUser.get() returns an object but the setText() wants a string as a parameter so
        //convert the object to string either by using toString() method or by concatinating "" empty string
        //but if you use toString() method in a case where new user has not yet updated his profile info then
        //get() method will return "null" object and null.toString() will give an exception and app will crush

        //but if you use null + "" , the app will not crush rather it will display null as string
        //so use the string concatination method to avoid exceptions

        if (parseUser.get("profileName") == null){
            //we use .equal() method to compare the value of objects
            //here we are comparing two objects
            //means if the returned object == null or no value
            edtProfileName.setText("");

        }else {
            //now it is safe to use toString() methods as get() will never return null object now
            edtProfileName.setText(parseUser.get("profileName").toString());
//            edtProfileBio.setText(parseUser.get("profileBio") + "");
//            edtProfileProfession.setText(parseUser.get("profileProfession") + "");
//            edtProfileHobbies.setText(parseUser.get("profileHobbies") + "");
//            edtProfileFavSport.setText(parseUser.get("profileFavSport") + "");
        }

        if (parseUser.get("profileBio") == null){
            edtProfileBio.setText("");
        }else {
            edtProfileBio.setText(parseUser.get("profileBio") + "");
        }

        if (parseUser.get("profileProfession") == null){
            edtProfileProfession.setText("");
        }else {
            edtProfileProfession.setText(parseUser.get("profileProfession") + "");
        }

        if (parseUser.get("profileHobbies") == null){
            edtProfileHobbies.setText("");
        }else {
            edtProfileHobbies.setText(parseUser.get("profileHobbies") + "");
        }

        if (parseUser.get("profileFavSport") == null){
            edtProfileFavSport.setText("");
        }else {
            edtProfileFavSport.setText(parseUser.get("profileFavSport") + "");
        }




        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parseUser.put("profileName", edtProfileName.getText().toString());
                parseUser.put("profileBio", edtProfileBio.getText().toString());
                parseUser.put("profileProfession", edtProfileProfession.getText().toString());
                parseUser.put("profileHobbies", edtProfileHobbies.getText().toString());
                parseUser.put("profileFavSport", edtProfileFavSport.getText().toString());

                //show updating progress dialog
                ProgressDialog updateInfoProgressDia = new ProgressDialog(getContext());
                updateInfoProgressDia.setMessage("Updating info...");
                updateInfoProgressDia.show();

                //put these data of the current user to the server

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null){
                            //there was no error to update/set the info to the parse server
                            FancyToast.makeText(getContext(),  "Profile Info Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            updateInfoProgressDia.dismiss();

                        }else {
                            FancyToast.makeText(getContext(),  e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                        }

                    }
                });

            }
        });

        return view;
        //inflater.inflate() will inflate the xml file to view object
        //container is the parent activity which will hold the fragment
        //attachToRoot == false as we attached the fragment manually to our own activity layout

    }

}