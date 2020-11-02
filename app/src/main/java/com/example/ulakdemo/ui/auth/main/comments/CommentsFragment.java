package com.example.ulakdemo.ui.auth.main.comments;

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
import com.example.ulakdemo.databinding.FragmentCommentsBinding;
import com.example.ulakdemo.ui.auth.main.posts.PostsFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentsFragment extends Fragment implements CommentsRecyclerAdapter.OnItemClickListener {

    private static final String TAG = "CommentsFragment";
    private FragmentCommentsBinding binding;
    private CommentsViewModel commentsViewModel;
    private View view;

    @Inject
    SessionManager sessionManager;

    @Inject
    CommentsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        commentsViewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
        binding = FragmentCommentsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        initRecyclerView();
        subscribeObservers();

        return view;
    }


    private void subscribeObservers() {
        commentsViewModel.observePost().removeObservers(getViewLifecycleOwner());
        commentsViewModel.observePost().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING:
                        binding.progressBarComments.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        adapter.setComments(listResource.data);
                        binding.progressBarComments.setVisibility(View.INVISIBLE);
                        break;
                    case ERROR:
                        binding.progressBarComments.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "An error occurred while trying to get comments.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Navigation.findNavController(view).navigate(R.id.action_postsFragment2_to_commentsFragment2);
        sessionManager.setCurrentPostId(position);
    }

    private void initRecyclerView() {
        binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewComments.setAdapter(adapter);
        adapter.setOnItemClickListener(CommentsFragment.this);
    }

    @Override
    public void onDestroyView() {
        commentsViewModel.observePost().removeObservers(getViewLifecycleOwner());
        binding.recyclerViewComments.setAdapter(null);
        Log.d(TAG, "onDestroyView: Temizlendi");
        super.onDestroyView();
    }
}