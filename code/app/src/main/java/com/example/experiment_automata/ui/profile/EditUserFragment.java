package com.example.experiment_automata.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;
import com.example.experiment_automata.backend.users.ContactInformation;
import com.example.experiment_automata.backend.users.User;

import javax.annotation.Nonnull;

/**
 * Role/Pattern:
 *
 *       This class provides the framework needed to make and edit an user's contact information.
 *
 * Known Issue:
 *
 *      1. None
 */

// Basic layout of this fragment inspired by lab work in CMPUT 301
// Abdul Ali Bangash, "Lab 3", 2021-02-04, Public Domain,
// https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
public class EditUserFragment extends DialogFragment {
    public static final String bundleUserKey = "USER";
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditUserFragment.OnFragmentInteractionListener listener;

    /**
     * This is an interface for any activity using this fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(User user, String name, String email, String phone);
    }

    /**
     * Creates a new instance of this fragment with a user.
     * @param user
     *  The user that will be edited
     * @return
     *  Fragment to edit the user's contact information
     */
    public static EditUserFragment newInstance(User user) {
        EditUserFragment fragment = new EditUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(bundleUserKey, user);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Identifies the listener for the fragment when it attaches.
     * @param context
     *  Context of the fragment
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditUserFragment.OnFragmentInteractionListener) {
            listener = (EditUserFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Gives instructions for when creating this dialog and prepares it's dismissal.
     * @param savedInstanceState
     *   allows you to pass information about a user
     * @return
     *   the dialog that will be created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nonnull Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user, null);

        // link all of the variables with their objects in the UI
        name = view.findViewById(R.id.edit_name);
        email = view.findViewById(R.id.edit_email);
        phone = view.findViewById(R.id.edit_phone);

        // prepare the UI elements with existing information
        User user = (User) getArguments().getSerializable(bundleUserKey);
        ContactInformation info = user.getInfo();
        name.setText(info.getName());
        email.setText(info.getEmail());
        phone.setText(info.getPhone());

        AlertDialog.Builder build = new AlertDialog.Builder(getContext());
        return build
                .setView(view)
                .setTitle("Edit Contact Information")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = name.getText().toString();
                        String newEmail = email.getText().toString();
                        String newPhone = phone.getText().toString();

                        listener.onOkPressed(user, newName, newEmail, newPhone);
                    }
                })
                .create();
    }
}