package com.example.ulakdemo.di;

import com.example.ulakdemo.ui.auth.main.comments.CommentsRecyclerAdapter;
import com.example.ulakdemo.ui.auth.main.posts.PostsRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.scopes.FragmentScoped;

@Module
@InstallIn(FragmentComponent.class)
public class FragmentModule {

    @FragmentScoped
    @Provides
    static PostsRecyclerAdapter providePostAdapter() {
        return new PostsRecyclerAdapter();
    }

    @FragmentScoped
    @Provides
    static CommentsRecyclerAdapter provideCommentAdapter() {
        return new CommentsRecyclerAdapter();
    }
}
