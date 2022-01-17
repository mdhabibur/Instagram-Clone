package com.mmali.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    //github personal access token: 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
    //github third personal access token: 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
    //fourth pat :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
    //why tokes is expiring after few commits?

    //Ui components
    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp, btnLogIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        setTitle("Sign Up");

        edtEmail = findViewById(R.id.edtLoginEmail);
        edtPassword = findViewById(R.id.edtEnterPassword);
        // sign up a user if he/she presses enter key/return key after filling up all the password field
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //int i = int keyCode
                if ( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    // means i(keyCode) == KEYCODE_ENTER and keyEvent.getAction() == KeyEvent.ACTION_DOWN means user has pressed "enter key" and for pressing enter key "ACTION_DOWN" is done
                    onClick(btnSignUp); // btnSignUp is also  a type of view


                }
                return false;
            }
        });


        edtUsername = findViewById(R.id.edtUsername);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);


        //for not letting the session token create problems
        //new session token is created everytime when a new user is signed up
        //so we need to handle the session token

        if (ParseUser.getCurrentUser() != null){
//            ParseUser.logOut();
            transitionToSocialMediaActivity();
            //that means if a new user signs up and then quit the app and after few time the users again comes to your app, he should not need to display the sign up screen again
            //rather he should be redirected to the dashboard/SocialMediaActivity and we can do so from here
            //because the session token is already created for that particular new user in the parse server when he signed up

        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSignUp:

                if (edtEmail.getText().toString().equals("") || edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
                    FancyToast.makeText(SignUp.this,  "Please fill up all the fields!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }else {

                    //sign up a user
                    final ParseUser appUser = new ParseUser();

                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    //show a progress bar until an user sign up is done
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + edtUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if ( e== null){
                                FancyToast.makeText(SignUp.this, appUser.getUsername() + " is signed up", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                //take the new user to the dashboard/SocialMediaActivity
                                transitionToSocialMediaActivity();

                            }else {
                                FancyToast.makeText(SignUp.this, appUser.getUsername() + "There was an error to sign up: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                            }

                            progressDialog.dismiss();

                        }
                    });


                }



                break;

            case R.id.btnLogin:

                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);

                break;

        }

    }

    //hide the keyboard whenever an user clicks in empty space anywhere on the screen
    public void rootLayoutTapped(View view){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            //hides the keyboard when users click on the empty space of the screen

            //but there is a bug in this technique. this method hide the keyboard from window whenever the keyboard is present on the screen but if there is no keyboard on the screen yet
            //users click on empty space the app crushes and it gives InvocationTargetException . but how to solve the bug?
            //the easiest and simplest way of solving any bug in android is to wrap the code with try-catch block and it will handle any type of exception
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    //take user to the dashboard when user is signed up
    private void transitionToSocialMediaActivity(){

        Intent intent = new Intent(SignUp.this, SocialMediaActivity.class);
        startActivity(intent);
        finish();
        //we need to finish the activity as soon as the user is transferred to dashboard because we don't want the user to go back again
        //to the signup interface when the user presses back button . it is a very important feature to app.
        //user's must not be taken to sign up or login activity when they press back button or want to leave the app.
        //only take them to signup or login interface at the first time or when they click to logout button

        //by calling finish() method we clear out the instance of that activity from the stack
    }


}