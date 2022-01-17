package com.mmali.instagramclone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.postImageItem){
            //allow user to capture an image , upload it to parse server and then share it
            if (Build.VERSION.SDK_INT >= 23 &&
                 checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3000);

            }else {
                captureImage();
            }

        }

        else  if ( item.getItemId() == R.id.logoutUserItem){
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(SocialMediaActivity.this, SignUp.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( requestCode == 3000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }

    private void captureImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //action and Uri object
        startActivityForResult(intent, 4000); //it will return a result
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //@Nullable Intent data this parameter means the data we get can be null . so we have to also fiter that data != null
        if (requestCode == 4000 && resultCode == RESULT_OK && data != null){
            //do something with the caught image
            //upload image into the server

            try {
                Uri capturedImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImage); //we got the bitmap object
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //need to convert the bitmap to byte array for uploading to server
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                //now after converting to byte array it is time to upload it to parse server
                ParseFile parseFile = new ParseFile("img.png", bytes); //it creates a file data type named img.png
                ParseObject parseObject = new ParseObject("Photo"); //it creates a "Photo" class
                parseObject.put("picture", parseFile);

                //these code creates a column of "picture" under "Photo" class and of "File" data type and of name "img.png"
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Loading...");
                dialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null){
                            FancyToast.makeText(SocialMediaActivity.this, "Picture is Uploaded!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                        }else {
                            FancyToast.makeText(SocialMediaActivity.this, "Unknown Error: " + e.getMessage() , FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                        }
                        dialog.dismiss();

                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}