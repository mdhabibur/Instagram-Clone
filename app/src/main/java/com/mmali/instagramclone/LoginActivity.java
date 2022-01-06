package com.mmali.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLoginActivity, btnSignUpLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        //after entering email and password if the user presses "enter key" then log in the user
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if ( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLoginActivity);
                }

                return false;
            }
        });

        btnLoginActivity = findViewById(R.id.btnLoginActivity);
        btnSignUpLoginActivity = findViewById(R.id.btnSignUpLoginActivity);

        btnLoginActivity.setOnClickListener(this);
        btnSignUpLoginActivity.setOnClickListener(this);


        //for now we are doing so to handle the session token, later we will change the code
        if (ParseUser.getCurrentUser() != null){
//            ParseUser.logOut();
            //means the new user is already logged in.so no need to display the login screen agian .just display the dashboard/SocialMediaActivity
            transitionToSocialMediaActivity();
        }



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnLoginActivity:


                if (edtLoginEmail.getText().toString().equals("") || edtLoginPassword.getText().toString().equals("")){

                    FancyToast.makeText(LoginActivity.this,  "Please fill up all the fields!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }else {

                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging...");
                    progressDialog.show();


                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(),
                            edtLoginPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                    if (user != null && e == null){
                                        FancyToast.makeText(LoginActivity.this, user.getUsername() + " is logged in", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        //take the user to the SocialMediaActivity/dashboard
                                        transitionToSocialMediaActivity();

                                    }else {
                                        FancyToast.makeText(LoginActivity.this, user.getUsername() + "There was an error to login: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                                    }

                                    progressDialog.dismiss();

                                }
                            });



                }


                break;

            case R.id.btnSignUpLoginActivity:

                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);

                break;

        }

    }

    public void onRootLayoutTapped(View view){

        try {
            //hide a keyboard if it is present on the screen when users clicks anywhere on the screen

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            //it will give an error when users clicks on empty screen when the keyboard is not present on the screen.so wrap with try-catch block
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    //take user to the dashboard when user is signed up
    private void transitionToSocialMediaActivity(){

        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }

}