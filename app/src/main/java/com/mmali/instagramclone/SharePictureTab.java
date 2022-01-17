package com.mmali.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class SharePictureTab extends Fragment implements View.OnClickListener {


    private ImageView imgShare;
    private EditText edtDescription;
    private Button btnShareImage;

    Bitmap receivedImageBitmap;


    public SharePictureTab() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgShare = view.findViewById(R.id.imgShare);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnShareImage = view.findViewById(R.id.btnShareImage);

        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgShare:
                //when the image is clicked first we need to explicitly call for the external storage permission as it is a dangerous permission
                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                    //if the os of user is >= 23 and if the user has not already given the permission then request for the permission at run time
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else {
                    getChosenImage();
                }

                break;

            case R.id.btnShareImage:

                //means user has selected an image so give option to upload it to server
                if (receivedImageBitmap != null){
                    if (edtDescription.getText().toString().equals("")){

                        FancyToast.makeText(getContext(), "Error: You must specify a description of the image", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                    }else {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray(); //convert to byte array

                        //upload the image to parse server
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null){
                                    FancyToast.makeText(getContext(), "Done", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                                }else {
                                    FancyToast.makeText(getContext(), "Unknown Error: " + e.getMessage() , FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                                }
                                dialog.dismiss();

                            }
                        });


                    }

                }else {
                    FancyToast.makeText(getContext(), "Error: You must select an image", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }


                break;
        }

    }

    private void getChosenImage() {
//        FancyToast.makeText(getContext(), "Now we can access the images", Toast.LENGTH_SHORT, FancyToast.SUCCESS,false).show();

        //permission got so now pick the image
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000); //this method will return a result with image or without

    }

    //when we request permission from user then for the result this onRequestPermissionResult() method is run
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //the size of grantResults[] array is > 0 because in 0 index one request permission [READ_EXTERNAL_STORAGE] reside
                //so grantResults[0]=READ_EXTERNAL_STORAGE

                getChosenImage(); //if users have given permission then call the getChosenImage() method for selecting image
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000){
            if (resultCode == Activity.RESULT_OK){

                //do something with your captured image
                try {
                    Uri selectedImage = data.getData(); //got the image from the data
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                             filePathColumn, null, null, null);
                    cursor.moveToFirst(); //access the first object
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex); //got the path of the picture/image
                    cursor.close(); //need to close the cursor

                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    imgShare.setImageBitmap(receivedImageBitmap); //got the image in bitmap and put it to imgShare ImageView
                    //this is the process to access an image of user's device from fragment and set the image to any custom image view




                }catch (Exception e){

                }


            }



        }


    }



}