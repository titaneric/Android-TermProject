package com.example.titaneric.termproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
    private Spinner county_spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Thread thread = new Thread(mutiThread);
            thread.start();

            try {
                thread.join();
                Log.d("Thread", "finished");
            } catch (InterruptedException e) {
                // ...
            }
        }
        String dbName = "danger";
        OpenDrawer(dbName, "危險水域");
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
                county_spin = (Spinner) findViewById(R.id.county_spin);
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
    private Runnable mutiThread = new Runnable() {
        public void run(){
            syncWeatherData();
        }
    };
    public void syncWeatherData(){
        WeatherData[] WDArray = new XMLparser().getWeatherDataArray();
        weatherDB db = new weatherDB(MainActivity.this);

        for(int i=0;i<WDArray.length;i++){
            db.insertData(WDArray[i].getLocation(), WDArray[i].getTimeRange(), WDArray[i].getWeather(), WDArray[i].getMaxT(),WDArray[i].getMinT(), WDArray[i].getComfortIndex(), WDArray[i].getDropPercent());
        }
        db.close();
    }
    public String catchWeather(String location){
        String weather = "";
        String timeRange = "明日白天";
        weatherDB db = new weatherDB(MainActivity.this);
        weather = db.lookForOtherAttribute(location, timeRange).get("weather").toString();
        db.close();
        return weather;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();
        if (id == R.id.swim) {
            OpenDrawer("swim", title);
        } else if (id == R.id.surf) {
            OpenDrawer("surf", title);
        } else if (id == R.id.dive) {
            OpenDrawer("dive", title);
        } else if (id == R.id.raft) {
            OpenDrawer("canoe", title);
        }
        else if (id == R.id.danger) {
            OpenDrawer("danger", title);
        }
        else if (id == R.id.about) {
            openInfo(title);
        }
        else if (id == R.id.creator) {
            openInfo(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openInfo(String title){
        Intent intent= new Intent(MainActivity.this,AppActivity.class);
        Bundle data=new Bundle();
        data.putString("Title", title);
        intent.putExtras(data);
        startActivity(intent);

    }
    public void OpenDrawer(final String idName, String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        //setSupportActionBar(toolbar);
        final String dbName = idName + ".sqlite";

        OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
        mDbHelper.createDatabase();
        mDbHelper.open();
        String[] tableList = mDbHelper.getTableName();
        mDbHelper.close();
        ArrayAdapter<String> county_List = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_s, tableList);
        final Spinner county_spin = (Spinner) findViewById(R.id.county_spin);
        county_spin.setAdapter(county_List);
        county_List.setDropDownViewResource(R.layout.spinner);
        county_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                View container = findViewById(R.id.contain);

                View content = findViewById(R.id.nav);
                TextView time = (TextView) content.findViewById(R.id.time);
                TextView weather = (TextView) content.findViewById(R.id.weather);
                if(idName.equals("swim") || idName.equals("danger")) {
                    county_spin.setEnabled(true);
                    time.setText("明日白天");
                    weather.setText(catchWeather(selectedItem));
                }
                else {
                    county_spin.setEnabled(false);

                if(idName == "swim" || idName == "danger") {
                    View content = findViewById(R.id.nav);
                    TextView time = (TextView) content.findViewById(R.id.time);
                    TextView weather = (TextView) content.findViewById(R.id.weather);
                    time.setText("白天");
                    weather.setText(catchWeather(selectedItem));
                }
                else{
                    View content = findViewById(R.id.nav);
                    TextView time = (TextView) content.findViewById(R.id.time);
                    TextView weather = (TextView) content.findViewById(R.id.weather);

                    time.setText("");
                    weather.setText("");
                }

                final ListView placeList = (ListView)container.findViewById(R.id.placeList);
                OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
                mDbHelper.createDatabase();
                mDbHelper.open();


                String[] placeArray= mDbHelper.getTestData(selectedItem);
                ListModel info_data[] = new ListModel[placeArray.length];
                for(int i=0;i<placeArray.length;i++){
                    info_data[i] = new ListModel(placeArray[i], getResources().getIdentifier(idName, "drawable", getPackageName()));
                }
                mDbHelper.close();
                MyAdaptor adaptor = new MyAdaptor(MainActivity.this, R.layout.node_view, info_data);
                //ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,placeArray);

                placeList.setAdapter(adaptor);
                placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent= new Intent(MainActivity.this,MapsActivity.class);
                        Bundle data=new Bundle();

                        ListModel list = (ListModel)parent.getItemAtPosition(position);
                        String placename= list.getText();
                        data.putString("Place",placename);
                        data.putString("dbname",idName);
                        data.putString("select",selectedItem);
                        intent.putExtras(data);
                        startActivity(intent);

                                /*
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
                        *//*
                        if(idName=="danger"){
                            ListModel list = (ListModel)parent.getItemAtPosition(position);
                            String placename= list.getText();
                            OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            HashMap rowList = mDbHelper.lookForOtherAttribute(placename, selectedItem);
                            mDbHelper.close();
                            String tar=list.getText();
                            String LatS=String.valueOf(rowList.get("latitude"));
                            String LogS=String.valueOf(rowList.get("longitude"));
                            Uri gmmIntentUri = Uri.parse("geo:"+LatS+","+LogS+"?q="+LatS+","+LogS+"("+tar+")");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                        else if(idName=="swim")
                        {
                            ListModel list = (ListModel)parent.getItemAtPosition(position);
                            String placename= list.getText();
                            OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            HashMap rowList = mDbHelper.lookForOtherAttribute(placename, selectedItem);
                            mDbHelper.close();
                            String tar=list.getText();
                            String LatS=String.valueOf(rowList.get("latitude"));
                            String LogS=String.valueOf(rowList.get("longitude"));
                            Uri gmmIntentUri = Uri.parse("geo:"+LatS+","+LogS+"?q="+placename);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                        else {
                            ListModel list = (ListModel)parent.getItemAtPosition(position);
                            String placename= list.getText();
                            OpenDataAdaptor mDbHelper = new OpenDataAdaptor(MainActivity.this, dbName);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            HashMap rowList = mDbHelper.lookForOtherAttribute_DSC(placename, selectedItem);
                            mDbHelper.close();
                            String Address = rowList.get("location").toString();
                            String tar=list.getText();
                            String LatS=String.valueOf(location.getLatitude());
                            String LogS=String.valueOf(location.getLongitude());
                            Uri gmmIntentUri = Uri.parse("geo:"+LatS+","+LogS+"?q="+Address+tar);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }*/
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
