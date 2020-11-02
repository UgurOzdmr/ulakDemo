package com.example.ulakdemo.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.ulakdemo.R;
import com.example.ulakdemo.databinding.ActivityAuthBinding;
import com.example.ulakdemo.databinding.ActivityMainBinding;
import com.example.ulakdemo.model.User;
import com.example.ulakdemo.ui.auth.main.MainActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";

    private AuthViewModel viewModel;
    private ActivityAuthBinding binding;

    @Inject
    Drawable logo;
    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        binding = ActivityAuthBinding.inflate(getLayoutInflater());

        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        binding.buttonLogin.setOnClickListener(view -> attemptLogin());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.progressBarLogin.setVisibility(View.INVISIBLE);

        setLogo();

        subscribeObservers();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void subscribeObservers() {
        viewModel.observeAuthState().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case LOADING:
                        showProgressBar(true);
                        break;
                    case ERROR:
                        showProgressBar(false);
                        Toast.makeText(AuthActivity.this, userAuthResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case AUTHENTICATED:
                        showProgressBar(false);
                        onLoginSuccess();
                        Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
                        break;
                    case NOT_AUTHENTICATED:
                        showProgressBar(false);
                        break;
                }
            }
        });
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            binding.progressBarLogin.setVisibility(View.VISIBLE);
        } else {
            binding.progressBarLogin.setVisibility(View.INVISIBLE);
        }
    }

    private void setLogo() {
        if(requestManager == null)
            return;
        requestManager
                .load(logo)
                .into((ImageView) findViewById(R.id.image_view_logo));
    }


    private void attemptLogin() {
        if (TextUtils.isEmpty(binding.textViewUsername.getText().toString())) {
            return;
        }

        viewModel.authenticateWithId(Integer.parseInt(binding.textViewUsername.getText().toString()));
    }
}
