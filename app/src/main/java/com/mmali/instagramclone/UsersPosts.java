package com.mmali.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        //initializing the LinearLayout
        linearLayout = findViewById(R.id.linearLayout);

        //retrieve the passed data from UsersTab Fragment
        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra("username");
        FancyToast.makeText(this, receivedUserName, Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

        //First set the title of the action bar of UsersPosts activity using the tapped user's username
        setTitle(receivedUserName + "'s posts");

        //retrieve the post of that user
        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");

        //giving the necessary condtions to the query
        parseQuery.whereEqualTo("username", receivedUserName);
        parseQuery.orderByDescending("createdAt"); //order by newest post first

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size() > 0 && e == null){
                    for (ParseObject post : objects){

                        TextView postDescription = new TextView(UsersPosts.this); //creates an object of TextView for holding some text
                        postDescription.setText(post.get("image_des") + ""); //gives the string representation of the returned object. toString() method may cause app crush in case of null object returned from the server

                        //getting the image(ParseFile) from the server
                        ParseFile postPicture = (ParseFile) post.get("picture"); //need to convert the returned object to of ParseFile type
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if (data != null && e == null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(UsersPosts.this);//creating ui element from java code

                                    LinearLayout.LayoutParams imageView_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    //sets the layout width and height for the postImageView ImageView inside the LinearLayout

                                    imageView_Params.setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(imageView_Params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);


                                    //defining dimensions and attributes for the postDescription textView here
                                    LinearLayout.LayoutParams  des_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5,5,5,5);

                                    postDescription.setLayoutParams(des_params);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.RED);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f); //here f defines it is a float number


                                    //now add the two objects to the linear layout
                                    //here order is very important  because it is a linear layout and it's orientation is set to vertical.so
                                    //the object we write first will be displayed to the top and then the second one , third ,,,

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);





                                }


                            }
                        });


                    }
                }else {
                    FancyToast.makeText(UsersPosts.this, receivedUserName + " does not have any posts!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    //and we also want to remove the instance of that empty activity from the user as there is no posts
                    finish();
                }

                dialog.dismiss();
            }
        });



    }


}