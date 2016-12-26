package com.example.titaneric.termproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Bundle data=this.getIntent().getExtras();
        View content = (View)findViewById(R.id.info);
        TextView info = (TextView) content.findViewById(R.id.infomation);
        String title = data.get("Title").toString();
        info.setText("From" + title);
    }
}
