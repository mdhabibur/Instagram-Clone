package com.mmali.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    //github personal access token: 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
    //github second token: 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

    //this repository link on github: https://github.com/mdhabibur/Instagram-Clone

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());


    }

}
