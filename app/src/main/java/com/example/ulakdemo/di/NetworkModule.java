package com.example.ulakdemo.di;

import com.example.ulakdemo.network.MainAPI;
import com.example.ulakdemo.network.UserAPI;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import retrofit2.Retrofit;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class NetworkModule {

    @ActivityRetainedScoped
    @Provides
    static UserAPI provideUserAPI(Retrofit retrofit) {
        return retrofit.create(UserAPI.class);
    }

    @ActivityRetainedScoped
    @Provides
    static MainAPI provideMainAPI(Retrofit retrofit) {
        return retrofit.create(MainAPI.class);
    };
}
