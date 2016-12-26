package com.example.titaneric.termproject;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TitanEric on 12/26/2016.
 */

public class MyAdaptor  extends ArrayAdapter<ListModel>{
    Context Mycontext;
    int myViewId;
    ListModel [] data = null;
    public MyAdaptor(Context context, int textViewResourceId, ListModel[] objects) {
        super(context, textViewResourceId, objects);
        Mycontext = context;
        myViewId = textViewResourceId;
        data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InfoHolder info = null;


        View row = ((Activity) Mycontext).getLayoutInflater().inflate(R.layout.node_view, parent, false);
        info = new InfoHolder();
        info.txtTitle = (TextView) row.findViewById(R.id.title);

        info.imgIcon= (ImageView) row.findViewById(R.id.image);
        row.setTag(info);


        ListModel model = data[position];
        info.imgIcon.setImageResource(model.Image);
        info.txtTitle.setText(model.Text);

        return row;
    }
    static class InfoHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
