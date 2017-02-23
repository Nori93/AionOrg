package com.example.norbert.aion;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class LoadingActivity extends Activity {

    //UI
    ImageView profil_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI
        profil_img = (ImageView)findViewById(R.id.log_iv);


    }

}
