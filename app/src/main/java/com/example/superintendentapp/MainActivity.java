package com.example.superintendentapp;

/* import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
* An activity that displays a Google map with a marker (pin) to indicate a particular location.
* /
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

public GoogleMap mMap;
public SupportMapFragment mapFragment;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
// Retrieve the content view that renders the map.
setContentView(R.layout.content_officer_page);
// Get the SupportMapFragment and request notification
// when the map is ready to be used.
mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

assert mapFragment != null;
mapFragment.getMapAsync(this);
}

/**
* Manipulates the map when it's available.
* The API invokes this callback when the map is ready to be used.
* This is where we can add markers or lines, add listeners or move the camera. In this case,
* we just add a marker near Sydney, Australia.
* If Google Play services is not installed on the device, the user receives a prompt to install
* Play services inside the SupportMapFragment. The API invokes this method after the user has
* installed Google Play services and returned to the app.
* /
@Override
public void onMapReady(GoogleMap googleMap) {
setContentView(R.layout.content_officer_page);

mMap = googleMap;
//        Polyline polyline = mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-35.016, 143.321),
//                        new LatLng(-34.747, 145.592),
//                        new LatLng(-34.364, 147.891),
//                        new LatLng(-33.501, 150.217),
//                        new LatLng(-32.306, 149.248),
//                        new LatLng(-32.491, 147.309)));
//

LatLng test = new LatLng(-23.32112,32.213);
mMap.addMarker(new MarkerOptions().title("test").position(test));

//        PolylineOptions polylineOptions = new PolylineOptions().clickable(true).add(
//                new LatLng(-35.016, 143.321),
//                new LatLng(-34.747, 145.592),
//                new LatLng(-34.364, 147.891),
//                new LatLng(-33.501, 150.217),
//                new LatLng(-32.306, 149.248),
//                new LatLng(-32.491, 147.309)
//        );

// polylineOptions.color(R.color.polylineRed);
// mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-33.501, 150.217)));
// mMap.addPolyline(polylineOptions);

}

@Override
public void onLocationChanged(Location location) {
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Changed"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
}

@Override
public void onStatusChanged(String s, int i, Bundle bundle) {

}

@Override
public void onProviderEnabled(String s) {

}

@Override
public void onProviderDisabled(String s) {

}

@Override
public void onResume() {
mapFragment.onResume();
super.onResume();
}

@Override
public void onPause() {
super.onPause();
mapFragment.onPause();
}

@Override
public void onDestroy() {
super.onDestroy();
mapFragment.onDestroy();
}

@Override
public void onLowMemory() {
super.onLowMemory();
mapFragment.onLowMemory();
}
}
*/

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.LogDescriptor;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_page);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        Log.d("CALLED", "HOLO");
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("HEREHERE", "HERE");
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
