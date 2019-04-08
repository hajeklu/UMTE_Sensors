package cz.uhk.fim.sensors.framets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import cz.uhk.fim.sensors.R;

public class GPSFragment extends Fragment implements OnSuccessListener<Location>, View.OnClickListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button start;
    private Button stop;
    private TextView textView;
    private LocationCallback locationCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        start = getView().findViewById(R.id.starGPS);
        stop = getView().findViewById(R.id.stopGPS);
        textView = getView().findViewById(R.id.fragment_gps_Text1);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this);
    }

    @Override
    public void onSuccess(Location location) {
        Toast.makeText(getContext(), location.getLatitude() + ": " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.starGPS) {
            startTracking();
        } else if (v.getId() == R.id.stopGPS) {
            stopTracking();
        }
    }


    @SuppressLint("MissingPermission")
    private void startTracking() {
        textView.setText("TRACKUJU..");
        final LocationRequest locationRequest = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);


        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLocations().size() > 0) {
                    Location location
                            = locationResult.getLocations().get(0);
                    textView.setText(location.getLatitude() + ": " + location.getLongitude());
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopTracking() {
        textView.setText("NETRACKUJU");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTracking();
    }
}
