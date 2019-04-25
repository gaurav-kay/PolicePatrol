package com.example.superintendentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleToIntFunction;

public class OfficerPage extends AppCompatActivity {

    private TextView FONameTextView;
    private TextView StatusTextView;
    private TextView CoordTextView;
    private String name;
    private String uid;
    private Button button;
    private ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FONameTextView = findViewById(R.id.FOName);
        StatusTextView = findViewById(R.id.status);
        CoordTextView = findViewById(R.id.coord);

        name = getIntent().getExtras().getString("USER");
        uid = getIntent().getExtras().getString("UID");

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfficerPage.this, NewMapActivity.class);

                intent.putExtra("Coords", result);

                startActivity(intent);
                // getSupportFragmentManager().beginTransaction().replace(R.id.linearlayout,new MapFragment()).commit();
            }
        });

        FONameTextView.setText(name);

        Trip trip = null;

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.geospark.co/v1/api/trip/?user_id="+uid;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Api-Key", "0a39fd0363c04286b0959cc50d6b9120")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    OfficerPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Trip trip = extractTripIDSFromJson(myResponse);
                            if (trip.getTripID().isEmpty()){
                                CoordTextView.setText("No Available Trips");
                            }
                            else {
                                setTripCoords(trip);
                            }
                            updateUI(trip);

                        }
                    });
                }
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void updateUI(Trip trip){
        if(trip.getEndAddress().isEmpty() && trip.getStartAddress().isEmpty()){
            StatusTextView.setText("INACTIVE");
        }
        else if(trip.getEndAddress().isEmpty() && !trip.getStartAddress().isEmpty()){
            StatusTextView.setText("ACTIVE");
        }else {
            StatusTextView.setText("INACTIVE");
        }
    }

    public void setTripCoords(Trip trip){

        final Trip tripp = trip;


        if(StatusTextView.getText()=="INACTIVE") {


            String url = "https://api.geospark.co/v1/api/trip/route?trip_id=" + trip.getTripID();

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Api-Key", "0a39fd0363c04286b0959cc50d6b9120")
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();

                        OfficerPage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<ArrayList<Double>> coordinates = extractTripCoords(myResponse);
                                tripp.setTripCoord(coordinates);
                                CoordTextView.setText(tripp.getTripCoord().toString());
                            }
                        });
                    }
                }
            });
        }
        else {
            String url = "https://api.geospark.co/v1/api/location/?user_id=" + uid;

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Api-Key", "0a39fd0363c04286b0959cc50d6b9120")
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();

                        OfficerPage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<ArrayList<Double>> coordinates = extractTripCoordsACTIVE(myResponse);
                                tripp.setTripCoord(coordinates);
                                CoordTextView.setText(tripp.getTripCoord().toString());
                            }
                        });
                    }
                }
            });
        }


    }

    public ArrayList<ArrayList<Double>> extractTripCoordsACTIVE(String response){
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        Log.e("RESPONSE",response);


        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(response);

            JSONObject data = baseJsonResponse.getJSONObject("data");

            JSONArray locations = data.getJSONArray("locations");

            for(int i=0; i<locations.length(); i++){

                JSONObject currentObject = locations.getJSONObject(i);
                JSONObject coordObj  = currentObject.getJSONObject("coordinates");
                JSONArray coordArray = coordObj.getJSONArray("coordinates");

                ArrayList<Double> coord = new ArrayList<Double>();


                for (int j= 0; j < coordArray.length(); j++) {
                    coord.add((Double) coordArray.get(j));
                }

                result.add(coord);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return result;


    }
    public ArrayList<ArrayList<Double>> extractTripCoords(String response){
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        Log.e("RESPONSE",response);

        ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();


        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(response);

            JSONArray data = baseJsonResponse.getJSONArray("data");

            Log.e("DATATRIPCOORDS",data.toString());


            // Extract the JSONArray associated with the key called "users",
            // which represents a list of users.
            for(int i=0; i<data.length(); i++) {
                JSONObject currentData = data.getJSONObject(i);

                JSONArray currentCoord = currentData.getJSONArray("coordinates");

                ArrayList<Double> coord = new ArrayList<Double>();

                for (int j= 0; j < currentCoord.length(); j++) {
                    coord.add((Double) currentCoord.get(j));
                }

                result.add(coord);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return result;

    }


    public static Trip extractTripIDSFromJson(String fieldofficerJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(fieldofficerJSON)) {
            return null;
        }

        Log.e("RESPONSE",fieldofficerJSON);

        Trip lastTrip = null;

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(fieldofficerJSON);

            JSONObject data = baseJsonResponse.getJSONObject("data");

            Log.e("DATA",data.toString());

            // Extract the JSONArray associated with the key called "users",
            // which represents a list of users.
            JSONArray tripsArray = data.getJSONArray("trips");
            if(tripsArray.length()==0){
                lastTrip = new Trip("","","","","", Double.parseDouble("0"),Double.parseDouble("0"));
                return lastTrip;
            }

            // For each officer in the officerArray, create an FieldOfficer object


            // Get a single officer at position i within the list of officers
            JSONObject currentTrip = tripsArray.getJSONObject(0);
            Log.e("CURRENTTRIP",currentTrip.toString());


            // For a given officer, extract the UID associated with the
            // officer.

            // Extract the value for the key called "id"
            String TripID = currentTrip.getString("trip_id");
            String startAdd = currentTrip.getString("start_address");
            String endAdd;
            String endTime;
            Double dur;
            Double dis;
            try {
                endAdd = currentTrip.getString("end_address");
                endTime = currentTrip.getString("trip_ended_at");
                dur = currentTrip.getDouble("duration");
                dis = currentTrip.getDouble("distance_covered");
            } catch(JSONException e){
                endAdd="";
                endTime="";
                dur=0.0;
                dis=0.0;
            }
            String startTime = currentTrip.getString("trip_started_at");




            lastTrip = new Trip(TripID,startAdd, endAdd,startTime,endTime,dur,dis);


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return lastTrip;
    }

}