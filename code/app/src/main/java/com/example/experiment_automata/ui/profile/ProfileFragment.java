package com.example.experiment_automata.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.experiment_automata.ui.NavigationActivity;
import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.users.ContactInformation;
import com.example.experiment_automata.backend.users.User;
import com.example.experiment_automata.ui.Screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Role/Pattern:
 *
 *  Displays a user profile with contact information
 *
 *  Known Issue:
 *
 *      1. No ability to edit information
 */
public class ProfileFragment extends Fragment {
    public static final String userKey = "user";
    private ProfileViewModel profileViewModel;
    private TextView nameView;
    private TextView emailView;
    private TextView phoneView;
    private FloatingActionButton fab;

    /**
     * Creates the fragment view.
     * @param inflater
     *  The inflater
     * @param container
     *  The container
     * @param savedInstanceState
     *  The saved instance state
     * @return
     *  The view of the fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NavigationActivity parentActivity = (NavigationActivity) getActivity();
        // Set current fragment in parent activity
        parentActivity.setCurrentScreen(Screen.Profile);
        parentActivity.setCurrentFragment(this);
        User user;
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(userKey);
        } else {
            user = parentActivity.loggedUser;
        }
        ContactInformation userInfo = user.getInfo();
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.setUser(user);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView idView = root.findViewById(R.id.profile_userid);
        idView.setText(user.getUserId().toString());
        nameView = root.findViewById(R.id.profile_username);
        emailView = root.findViewById(R.id.profile_email);
        phoneView = root.findViewById(R.id.profile_phone);
        final ImageButton editButton = root.findViewById(R.id.profile_edit_button);
        profileViewModel.getName().observe(getViewLifecycleOwner(), nameView::setText);
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), emailView::setText);
        profileViewModel.getPhone().observe(getViewLifecycleOwner(), phoneView::setText);
        editButton.setOnClickListener(v -> {
            Fragment editUserFragment = new EditUserFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(EditUserFragment.bundleUserKey, user);
            editUserFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(editUserFragment, "USER").commit();
        });
        fab = parentActivity.findViewById(R.id.fab_button);
        fab.setVisibility(View.GONE);
        return root;
    }

    /**
     * Runs when the fragment is paused in the lifecycle.
     */
    public void onPause() {
        super.onPause();
        fab.setVisibility(View.VISIBLE);
    }

    /**
     * Update views on screen.
     */
    public void update() {
        nameView.setText(profileViewModel.getName().getValue());
        emailView.setText(profileViewModel.getEmail().getValue());
        phoneView.setText(profileViewModel.getPhone().getValue());
    }
}