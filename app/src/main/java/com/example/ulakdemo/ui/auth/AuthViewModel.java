package com.example.ulakdemo.ui.auth;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.example.ulakdemo.SessionManager;
import com.example.ulakdemo.model.User;
import com.example.ulakdemo.network.UserAPI;


import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AuthViewModel extends ViewModel {

    private final UserAPI userAPI;
    private SessionManager sessionManager;

    @ViewModelInject
    public AuthViewModel(UserAPI userAPI, SessionManager sessionManager) {
        this.userAPI = userAPI;
        this.sessionManager = sessionManager;
    }

    public void authenticateWithId(int userId) {
        Log.d(TAG, "authenticateWithId - userId: " + (userId));
        sessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<User>> queryUserId(int userId) {
        return LiveDataReactiveStreams.fromPublisher(
                userAPI.getUser(userId)
                        // If error happens.
                        .onErrorReturn(throwable -> {
                            Log.d(TAG, "apply: " + throwable.getMessage());
                            User errorUser = new User();
                            errorUser.setId(-1);
                            return errorUser;
                        })

                        // If the userId is -1 returns AuthResource.error, if there is no error returns Authenticated
                        .map(user -> {
                            if (user.getId() == -1) {
                                return AuthResource.error("Could not authenticate", (User) null);
                            }
                            return AuthResource.authenticated(user);
                        })
                        .subscribeOn(Schedulers.io()));
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }
}
