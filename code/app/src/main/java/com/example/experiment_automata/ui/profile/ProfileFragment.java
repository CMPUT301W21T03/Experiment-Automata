package com.example.experiment_automata.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.experiment_automata.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.UserInformation.ContactInformation;
import com.example.experiment_automata.UserInformation.User;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NavigationActivity parentActivity = (NavigationActivity) getActivity();
        User user = parentActivity.loggedUser;
        ContactInformation userInfo = user.getInfo();
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView idView = root.findViewById(R.id.profile_userid);
        idView.setText(user.getUserId().toString());
        final TextView nameView = root.findViewById(R.id.profile_username);
        final TextView emailView = root.findViewById(R.id.profile_email);
        final TextView phoneView = root.findViewById(R.id.profile_phone);
        nameView.setText(userInfo.getName());
        emailView.setText(userInfo.getEmail());
        phoneView.setText(userInfo.getPhone());
        profileViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                nameView.setText(s);
            }
        });
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emailView.setText(s);
            }
        });
        profileViewModel.getPhone().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                phoneView.setText(s);
            }
        });
        return root;
    }
}