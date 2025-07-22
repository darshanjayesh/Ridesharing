package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;




public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutocompleteSupportFragment startLocationFragment;
    private AutocompleteSupportFragment destinationFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Places Autocomplete
        startLocationFragment = (AutocompleteSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment_start);
        destinationFragment = (AutocompleteSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment_destination);

        if (startLocationFragment != null) {
            startLocationFragment.setHint("Enter pickup location");
        }

        if (destinationFragment != null) {
            destinationFragment.setHint("Enter destination");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add your map setup code here
    }
} 