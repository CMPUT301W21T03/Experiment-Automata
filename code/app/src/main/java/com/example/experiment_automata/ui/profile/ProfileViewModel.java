package com.example.experiment_automata.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.experiment_automata.backend.users.User;

/**
 * Role/Pattern:
 *     Test fragment that might be removed at some point in the future.
 *
 *  Known Issue:
 *
 *      1. None
 */
public class ProfileViewModel extends ViewModel {
    private User user;
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> phone;

    public ProfileViewModel() {
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        phone = new MutableLiveData<>();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LiveData<String> getName() {
        username.setValue(user.getInfo().getName());
        return username;
    }

    public LiveData<String> getEmail() {
        email.setValue(user.getInfo().getEmail());
        return email;
    }

    public LiveData<String> getPhone() {
        phone.setValue(user.getInfo().getPhone());
        return phone;
    }
}