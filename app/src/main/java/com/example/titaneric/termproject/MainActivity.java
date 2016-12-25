package com.example.titaneric.termproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    //public static String[] introList = {"Start the Game","Score board",  "Help"};
    boolean GPS = false;
    private LocationManager lms;
    private Location location;
    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("Main");
        setSupportActionBar(toolbar);
        final String dbName = "danger";
        OpenDrawer(dbName);
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
           if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
               while (location==null) {
                  // lms.requestLocationUpdates(LocationManager.GPS_PROVIDER,600000,1,LocationListener);
                   location = getLastKnownLocation();
               }//使用GPS定位座標
         }


    }
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(GPS){
                GPS=false;
                Toast.makeText(getApplicationContext(),"關閉GPS定位",Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_gps_off_black_48dp);
                Spinner county_spin = (Spinner) findViewById(R.id.county_spin);
                county_spin.setEnabled(true);
            }
            else {
                LocationManager l = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!l.isProviderEnabled(l.GPS_PROVIDER))
                {
                    Toast.makeText(getApplicationContext(),"請先開啟GPS",Toast.LENGTH_SHORT).show();
                }
                else {
                    GPS=true;
                    item.setIcon(R.drawable.ic_gps_fixed_black_48dp);
                    Toast.makeText(getApplicationContext(),"開啟GPS定位",Toast.LENGTH_SHORT).show();
                    Spinner county_spin = (Spinner) findViewById(R.id.county_spin);
                    county_spin.setEnabled(false);
                    Geocoder g=new Geocoder(getApplicationContext(), Locale.TRADITIONAL_CHINESE);
                   // Location Loc=new Location(LOCATION_SERVICE);
                    List<Address> lstAddress;
                    try {
                        if(location!=null) {
                            lstAddress = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String city = lstAddress.get(0).getAdminArea();
                            Toast.makeText(getApplicationContext(), city, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String subTitle = item.getTitle().toString();
        if (id == R.id.swim) {
            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //toolbar.setSubtitle(subTitle);
            //setSupportActionBar(toolbar);
            OpenDrawer("swim");

        } else if (id == R.id.surf) {
            OpenDrawer("surf");
        } else if (id == R.id.dive) {
            OpenDrawer("dive");
        } else if (id == R.id.raft) {
            OpenDrawer("canoe");
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void OpenDrawer(final String idName){
        final String dbName = idName + ".sqlite";
        OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
        mDbHelper.createDatabase();
        mDbHelper.open();
        String[] tableList = mDbHelper.getTableName();
        mDbHelper.close();
        ArrayAdapter<String> county_List = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, tableList);
        Spinner county_spin = (Spinner) findViewById(R.id.county_spin);
        county_spin.setAdapter(county_List);
        county_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                View container = findViewById(R.id.contain);
                ListView placeList = (ListView)container.findViewById(R.id.placeList);
                OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
                mDbHelper.createDatabase();
                mDbHelper.open();
                String[] placeArray= mDbHelper.getTestData(selectedItem);
                mDbHelper.close();
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,placeArray);
                placeList.setAdapter(adapter);
                placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent= new Intent(MainActivity.this,MapsActivity.class);
                        Bundle data=new Bundle();
                        String placename= parent.getItemAtPosition(position).toString();
                        OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
                        mDbHelper.createDatabase();
                        mDbHelper.open();
                        HashMap rowList = mDbHelper.lookForOtherAttribute(placename, selectedItem);
                        mDbHelper.close();
                        data.putSerializable("HashMap", rowList);
                        data.putBoolean("GPS",GPS);
                        data.putString("dbName", idName);
                        data.putDouble("Lat",location.getLatitude());
                        data.putDouble("Lon",location.getLongitude());
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                });
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {	//定位狀態改變
        //status=OUT_OF_SERVICE 供應商停止服務
        //status=TEMPORARILY_UNAVAILABLE 供應商暫停服務
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_SHORT).show();

        if(GPS) {
            GPS = false;
            //Toast.makeText(getApplicationContext(), "關閉GPS定位", Toast.LENGTH_SHORT).show();
            findViewById(R.id.action_settings).setBackgroundResource(R.drawable.ic_gps_off_black_48dp);
            Spinner county_spin = (Spinner) findViewById(R.id.county_spin);
            county_spin.setEnabled(true);
        }
    }


}