package com.example.androidchallenge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchallenge.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO (candidate): call the Greeter.SayHello gRPC service and show the reply on screen.
        // Server: ./gradlew :server:run  (emulator reaches host at 10.0.2.2:50051, plaintext)
    }
}
