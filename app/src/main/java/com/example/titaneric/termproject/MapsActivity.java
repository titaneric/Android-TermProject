package com.example.titaneric.termproject;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity {


	private String Place;
	private String idName;
    private String dbName;
	private TextView Title;
	private TextView Content;
	private String selectedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.

		Bundle data=this.getIntent().getExtras();

        idName = data.getString("dbname");
		selectedItem=data.getString("select");
		Place=data.getString("Place");
		dbName=idName+".sqlite";
		Title=(TextView)findViewById(R.id.place);
		Content=(TextView)findViewById(R.id.otherInfo);
		Title.setText(Place);
        if(idName.equals("danger")){
            OpenDataAdaptor mDbHelper = new OpenDataAdaptor(this, dbName);
            mDbHelper.createDatabase();
            mDbHelper.open();
            HashMap rowList = mDbHelper.lookForOtherAttribute(Place, selectedItem);
            mDbHelper.close();
            String LatS=String.valueOf(rowList.get("緯度"));
            String LogS=String.valueOf(rowList.get("經度"));
            Content.setText("緯度: "+LatS+"\n經度: " + LogS);
        }
        else if(idName.equals("swim"))
        {
            OpenDataAdaptor mDbHelper = new OpenDataAdaptor(this, dbName);
            mDbHelper.createDatabase();
            mDbHelper.open();
            HashMap rowList = mDbHelper.lookForOtherAttribute(Place, selectedItem);
            mDbHelper.close();
            String LatS=String.valueOf(rowList.get("緯度"));
            String LogS=String.valueOf(rowList.get("經度"));
            Content.setText("備註: " + String.valueOf(rowList.get("敘述")) + "\n緯度: "+LatS+"\n經度: " + LogS);
        }
        else {
            OpenDataAdaptor mDbHelper = new OpenDataAdaptor(this, dbName);
            mDbHelper.createDatabase();
            mDbHelper.open();
            HashMap rowList = mDbHelper.lookForOtherAttribute_DSC(Place, selectedItem);
            mDbHelper.close();
            String Address = rowList.get("店址").toString();
            Content.setText("所有人: "+String.valueOf(rowList.get("所有人"))+ "\n電話: " + String.valueOf(rowList.get("電話"))+ "\n店址: " + Address);
        }



	}
	public void OpenMap(View view)
	{
		if(idName.equals("danger")){
			OpenDataAdaptor mDbHelper = new OpenDataAdaptor(this, dbName);
			mDbHelper.createDatabase();
			mDbHelper.open();
			HashMap rowList = mDbHelper.lookForOtherAttribute(Place, selectedItem);
			mDbHelper.close();
			String LatS=String.valueOf(rowList.get("緯度"));
			String LogS=String.valueOf(rowList.get("經度"));
			//Content.setText("緯度: "+LatS+"\n + 經度: " + LogS);
			Uri gmmIntentUri = Uri.parse("geo:"+LatS+","+LogS+"?q="+LatS+","+LogS+"("+Place+")");
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
		}
		else if(idName.equals("swim"))
		{
			OpenDataAdaptor mDbHelper = new OpenDataAdaptor(this, dbName);
			mDbHelper.createDatabase();
			mDbHelper.open();
			HashMap rowList = mDbHelper.lookForOtherAttribute(Place, selectedItem);
			mDbHelper.close();
			String LatS=String.valueOf(rowList.get("緯度"));
			String LogS=String.valueOf(rowList.get("經度"));
            //Content.setText("備註: " + String.valueOf(rowList.get("敘述")) + "\n緯度: "+LatS+"\n + 經度: " + LogS);
            Uri gmmIntentUri = Uri.parse("geo:"+LatS+","+LogS+"?q="+Place);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
		}
		else {
			OpenDataAdaptor mDbHelper = new OpenDataAdaptor(this, dbName);
			mDbHelper.createDatabase();
			mDbHelper.open();
			HashMap rowList = mDbHelper.lookForOtherAttribute_DSC(Place, selectedItem);
			mDbHelper.close();
			String Address = rowList.get("店址").toString();
            //Content.setText("所有人: "+String.valueOf(rowList.get("所有人"))+ "\n電話: " + String.valueOf(rowList.get("電話"))+ "店址: " + Address);


            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Address+Place);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
		}
	}


}
