package com.example.ulakdemo.ui.auth.main.posts;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ulakdemo.SessionManager;
import com.example.ulakdemo.model.Post;
import com.example.ulakdemo.network.MainAPI;
import com.example.ulakdemo.ui.auth.main.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostsViewModel extends ViewModel {

    private final MainAPI mainAPI;
    private SessionManager sessionManager;

    private MediatorLiveData<Resource<List<Post>>> posts;

    @ViewModelInject
    public PostsViewModel(MainAPI mainAPI, SessionManager sessionManager) {
        this.mainAPI = mainAPI;
        this.sessionManager = sessionManager;
    }


    public LiveData<Resource<List<Post>>> observePost() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading(null));

            final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(
                    mainAPI.getPosts(sessionManager.getAuthUser().getValue().data.getId())

                            .onErrorReturn(throwable -> {
                                Log.e(TAG, "apply: ",throwable );
                                Post post = new Post();
                                post.setId(-1);
                                ArrayList<Post> posts = new ArrayList<>();
                                posts.add(post);
                                return posts;
                            })
                            .map((Function<List<Post>, Resource<List<Post>>>) posts -> {
                                if (posts.size() > 0) {
                                    if (posts.get(0).getId() == -1) {
                                        return Resource.error("Something went wrong...", null);
                                    }
                                }

                                return Resource.success(posts);
                            })
                            .subscribeOn(Schedulers.io())
            );
            posts.addSource(source, listResource -> {
                posts.setValue(listResource);
                posts.removeSource(source);
            });

        }
        return posts;
    }
}
