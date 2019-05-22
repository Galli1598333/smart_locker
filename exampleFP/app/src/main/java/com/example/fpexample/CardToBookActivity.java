package com.example.fpexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CardToBookActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_tobook_card);

        String parkName = getIntent().getStringExtra("parkName");

        TextView parkLabel = (TextView) findViewById(R.id.idParkName);
        ImageView parkMap = (ImageView) findViewById(R.id.idParkImg);

        int img_source;

        parkLabel.setText(parkName);

        if(parkName.equals("Parco della Caffarella")){
            img_source = getResources().getIdentifier("@drawable/caffarellapark", null, this.getPackageName());
        }
        else if(parkName.equals("Villa Borghese")){
            img_source = getResources().getIdentifier("@drawable/villaborghese", null, this.getPackageName());
        }
        else{
            img_source = getResources().getIdentifier("@drawable/error", null, this.getPackageName());
        }

        parkMap.setImageResource(img_source);


    }
}
