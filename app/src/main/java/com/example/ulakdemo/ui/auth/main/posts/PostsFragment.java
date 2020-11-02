package com.example.ulakdemo.ui.auth.main.posts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ulakdemo.R;
import com.example.ulakdemo.SessionManager;
import com.example.ulakdemo.databinding.FragmentPostsBinding;
import com.example.ulakdemo.ui.auth.main.comments.CommentsViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostsFragment extends Fragment implements  PostsRecyclerAdapter.OnItemClickListener {

    private static final String TAG = "PostsFragment";
    private FragmentPostsBinding binding;
    private PostsViewModel postsViewModel;
    private View view;

    @Inject
    SessionManager sessionManager;

    @Inject
    PostsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPostsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

        initRecyclerView();
        subscribeObservers();

        return view;
    }

    private void subscribeObservers() {
        postsViewModel.observePost().removeObservers(getViewLifecycleOwner());
        postsViewModel.observePost().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING:
                        binding.progressBarPosts.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        adapter.setPosts(listResource.data);
                        binding.progressBarPosts.setVisibility(View.INVISIBLE);
                        break;
                    case ERROR:
                        binding.progressBarPosts.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "An error occurred while trying to get posts.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        sessionManager.setCurrentPostId(position);
        Navigation.findNavController(view).navigate(R.id.action_postsFragment2_to_commentsFragment2);
    }

    private void initRecyclerView() {
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPosts.setAdapter(adapter);
        adapter.setOnItemClickListener(PostsFragment.this);
    }

    @Override
    public void onDestroyView() {
        postsViewModel.observePost().removeObservers(getViewLifecycleOwner());
        binding.recyclerViewPosts.setAdapter(null);
        Log.d(TAG, "onDestroyView: Temizlendi");
        super.onDestroyView();
    }
}