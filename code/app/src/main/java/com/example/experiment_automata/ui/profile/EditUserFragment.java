package com.example.experiment_automata.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.experiment_automata.R;
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
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditUserFragment.OnFragmentInteractionListener listener;

    /**
     * This is an interface for any activity using this fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(String name, String email, String phone);
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
        args.putSerializable("USER", user);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(@Nonnull Bundle savedInstanceState) {
        AlertDialog.Builder build = new AlertDialog.Builder(getContext());
        return build.create();
    }
}