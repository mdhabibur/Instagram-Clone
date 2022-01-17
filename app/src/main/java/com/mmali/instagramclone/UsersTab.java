package com.mmali.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;


    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);

        listView.setOnItemClickListener(UsersTab.this); //instance of this UsersTab.this will handle the event
        listView.setOnItemLongClickListener(UsersTab.this);

        TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);


        //query through the parse server and fetch all the users and populate the list view with the users
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()); //don't want the current user object
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null){
                    if (objects.size() > 0){
                        for (ParseUser user : objects){
                            arrayList.add(user.getUsername());
                            //we got the parse user (model) and list view but  model and view can't interact directly so we
                            //need a controller (adapter)
                            //so this process is called model view controller(MVC) architechture

                        }
                        listView.setAdapter(arrayAdapter);
                        txtLoadingUsers.animate().alpha(0).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);

                    }
                }

            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //retrieve the user and pass him/her to the UsersPosts Activity so that we can work further
        Intent intent = new Intent(getContext(), UsersPosts.class);
        intent.putExtra("username", arrayList.get(i));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(i));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null && e == null){
//                    FancyToast.makeText(getContext(), user.get("profileProfession") + "", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());

                    prettyDialog.setTitle(user.getUsername() + " 's Info")
                            .setMessage(user.get("profileBio") + "\n"
                                      + user.get("profileProfession") + "\n"
                                      + user.get("profileHobbies") + "\n"
                                      + user.get("profileFavSport"))
                            .setIcon(R.drawable.person)
                            .addButton(
                                    "OK",
                                    R.color.black,
                                    R.color.pdlg_color_yellow,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            //dismiss the dialog
                                            prettyDialog.dismiss();
                                        }
                                    }
                            ).show();



                }

            }
        });

        //if you don't return true for this boolean returned type method the method will not work properly

        //when we wrote return false;
        //onItemLongClicked worked but it also took me to the UsersPosts activity but users don't want that
        //but when we wrote return true;
        //then onItemLongClicked worked properly
        return true;
    }
}