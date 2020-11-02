package com.example.ulakdemo.ui.auth.main.comments;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ulakdemo.SessionManager;
import com.example.ulakdemo.model.Comment;
import com.example.ulakdemo.network.MainAPI;
import com.example.ulakdemo.ui.auth.main.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CommentsViewModel extends ViewModel {

    private final MainAPI mainAPI;
    private SessionManager sessionManager;
    private MediatorLiveData<Resource<List<Comment>>> comments;
    private int postId;

    @ViewModelInject
    public CommentsViewModel(MainAPI mainAPI, SessionManager sessionManager) {
        this.mainAPI = mainAPI;
        this.sessionManager = sessionManager;
        postId = sessionManager.getCurrentPostId();
    }

    public LiveData<Resource<List<Comment>>> observePost() {
        if (comments == null) {
            comments = new MediatorLiveData<>();
            comments.setValue(Resource.loading(null));

            Log.d(TAG, "observePost: " + sessionManager.getCurrentPostId());

            final LiveData<Resource<List<Comment>>> source = LiveDataReactiveStreams.fromPublisher(
                    mainAPI.getComments(sessionManager.getCurrentPostId())
                            .onErrorReturn(throwable -> {
                                Log.e(TAG, "apply: ",throwable );
                                Comment comment = new Comment();
                                comment.setId(-1);
                                ArrayList<Comment> comments = new ArrayList<>();
                                comments.add(comment);
                                return comments;
                            })
                            .map((Function<List<Comment>, Resource<List<Comment>>>) comments -> {
                                if (comments.size() > 0) {
                                    if (comments.get(0).getId() == -1) {
                                        return Resource.error("Something went wrong...", null);
                                    }
                                }

                                return Resource.success(comments);
                            })
                            .subscribeOn(Schedulers.io())
            );
            comments.addSource(source, listResource -> {
                comments.setValue(listResource);
                comments.removeSource(source);
            });

        }
        return comments;
    }
}
