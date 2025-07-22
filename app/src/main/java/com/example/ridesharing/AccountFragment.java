package com.example.ridesharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;




public class AccountFragment extends Fragment {

    private TextView userName, userPhone, userEmail;
    private Button editProfileButton, logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        userName = view.findViewById(R.id.user_name);
        userPhone = view.findViewById(R.id.user_phone);
        userEmail = view.findViewById(R.id.user_email);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        logoutButton = view.findViewById(R.id.logout_button);

        // Load user data
        loadUserData();

        // Set up button clicks
        editProfileButton.setOnClickListener(v -> {
            // TODO: Implement edit profile functionality
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), loginActivity.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void loadUserData() {
        // TODO: Load user data from Firebase or local storage
        // This is where you'll populate the TextViews with user information
    }
} 