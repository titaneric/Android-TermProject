package com.example.titaneric.termproject;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;

	private String Name;
	private HashMap rowList;
    private String dbName;
    private String[] latlonList = {"swim", "danger"};
    private String[] locationList = {"dive", "canoe", "surf"};
	private boolean GPS;
	private double Lon,Lat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		Bundle data=this.getIntent().getExtras();
		rowList = (HashMap) data.getSerializable("HashMap");
        dbName = data.getString("dbName");
		Name=rowList.get("location").toString();
		GPS=data.getBoolean("GPS");
		Lat=data.getDouble("Lat");
		Lon=data.getDouble("Lon");
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		// Add a marker in Sydney and move the camera
        if(Arrays.asList(latlonList).contains(dbName)) {
            double latitude =  Double.parseDouble(rowList.get("latitude").toString());
            double longitude =  Double.parseDouble(rowList.get("longitude").toString());
            LatLng location = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            mMap.addMarker(new MarkerOptions().position(location).title(Name));
			if(GPS)
			{
				//Location l=new Location(LOCATION_SERVICE);
				LatLng self =new LatLng(Lat,Lon);
				mMap.addMarker(new MarkerOptions().position(self).title("You"));
			}
        }
        else if(Arrays.asList(locationList).contains(dbName)) {

        }

	}
}
