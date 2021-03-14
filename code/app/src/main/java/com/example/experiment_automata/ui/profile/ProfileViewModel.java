package com.example.experiment_automata.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.experiment_automata.UserInformation.User;

import java.util.UUID;

public class ProfileViewModel extends ViewModel {

    private User user;
    private MutableLiveData<String> username;
    private MutableLiveData<String> email;
    private MutableLiveData<String> phone;

    public ProfileViewModel() {
        user = new User();
        username = new MutableLiveData<>();
//        username.setValue(user.getInfo().getName());
        email = new MutableLiveData<>();
//        email.setValue(user.getInfo().getPhone());
        phone = new MutableLiveData<>();
//        phone.setValue(user.getInfo().getEmail());
    }

    public LiveData<String> getName() {
        return username;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getPhone() {
        return phone;
    }
}