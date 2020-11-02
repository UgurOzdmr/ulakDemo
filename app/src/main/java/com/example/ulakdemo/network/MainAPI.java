package com.example.ulakdemo.network;

import com.example.ulakdemo.model.Comment;
import com.example.ulakdemo.model.Post;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainAPI {

    @GET("posts")
    Flowable<List<Post>> getPosts(
            @Query("userId") int id
    );

    @GET("comments")
    Flowable<List<Comment>> getComments(
            @Query("postId") int id
    );

    @GET("posts/{id}")
    Flowable<Post> getPost(
            @Path("id") int postsId
    );
}
