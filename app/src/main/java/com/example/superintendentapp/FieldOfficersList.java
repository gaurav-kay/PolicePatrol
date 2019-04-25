package com.example.superintendentapp;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class FieldOfficersList extends AppCompatActivity {

    private TextView mTextViewResult;
    private FieldOfficerAdapter mAdapter;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_officers_list);

        ListView FOListView = (ListView) findViewById(R.id.list);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        mAdapter = new FieldOfficerAdapter(this, new ArrayList<FieldOfficerDetails>());

        FOListView.setAdapter(mAdapter);

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.geospark.co/v1/api/user/";

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

                    FieldOfficersList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<FieldOfficerDetails> officers = extractFeatureFromJson(myResponse);
                            assert officers != null;
                            mAdapter.addAll(officers);
                        }
                    });
                }
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpClient client = new OkHttpClient();

                String url = "https://api.geospark.co/v1/api/user/";

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

                            FieldOfficersList.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<FieldOfficerDetails> officers = extractFeatureFromJson(myResponse);
                                    mAdapter.clear();
                                    mAdapter.addAll(officers);
                                    pullToRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }
                });
            }
        });

        FOListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FieldOfficerDetails fieldOfficer = mAdapter.getItem(position);

                String name = fieldOfficer.getName();
                String uid = fieldOfficer.getUid();

                Intent i = new Intent(getApplicationContext(), OfficerPage.class);

                i.putExtra("USER",name);
                i.putExtra("UID",uid);
                startActivity(i);

            }
        });
    }



    public static List<FieldOfficerDetails> extractFeatureFromJson(String fieldofficerJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(fieldofficerJSON)) {
            return null;
        }

        Log.e("RESPONSE",fieldofficerJSON);
        // Create an empty ArrayList that we can start adding earthquakes to
        List<FieldOfficerDetails> officers = new ArrayList<>();

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
            JSONArray officerArray = data.getJSONArray("users");

            // For each officer in the officerArray, create an FieldOfficer object
            for (int i = 0; i < officerArray.length(); i++) {

                // Get a single officer at position i within the list of officers
                JSONObject currentOfficer = officerArray.getJSONObject(i);

                // For a given officer, extract the UID associated with the
                // officer.

                // Extract the value for the key called "id"
                String uid = currentOfficer.getString("id");


                String name = currentOfficer.getString("description");
                Log.e("NAME",name);

                // Create a new FieldOfficer object with the uid
                FieldOfficerDetails officer = new FieldOfficerDetails(uid,name);

                // Add the new {@link Earthquake} to the list of earthquakes.
                officers.add(officer);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return officers;
    }
}