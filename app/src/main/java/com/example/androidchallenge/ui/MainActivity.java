package com.example.androidchallenge.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidchallenge.databinding.ActivityMainBinding;

/** Dumb view: forwards input to the ViewModel and renders {@link GreetingUiState}. */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GreetingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, GreetingViewModel.factory()).get(GreetingViewModel.class);

        binding.greetButton.setOnClickListener(v ->
                viewModel.onGreetClicked(String.valueOf(binding.nameInput.getText())));
        viewModel.getUiState().observe(this, this::render);
    }

    private void render(GreetingUiState state) {
        binding.progress.setVisibility(state.loading ? View.VISIBLE : View.GONE);
        binding.greetButton.setEnabled(!state.loading);
        binding.resultText.setText(state.message == null ? "" : state.message);
        binding.nameLayout.setError(state.error);
    }
}
