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
        TextView titleText = (TextView) content.findViewById(R.id.title);

        TextView info = (TextView) content.findViewById(R.id.information);
        String title = data.get("Title").toString();
        titleText.setText(title);
        if(title.equals("關於APP")) {
            info.setText(R.string.app_info);
        }
        else if(title.equals("關於作者")){
            info.setText(R.string.creator_info);
        }
    }
}
