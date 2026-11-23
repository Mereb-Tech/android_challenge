package com.example.androidchallenge.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidchallenge.BuildConfig;
import com.example.androidchallenge.data.GreeterException;
import com.example.androidchallenge.data.GreeterRepository;
import com.example.androidchallenge.data.GrpcGreeterRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

/** Holds screen state, survives rotation, and talks only to {@link GreeterRepository}. */
public class GreetingViewModel extends ViewModel {

    static final String EMPTY_NAME_ERROR = "Please enter a name";

    private final GreeterRepository repository;
    private final Executor backgroundExecutor;
    private final MutableLiveData<GreetingUiState> uiState =
            new MutableLiveData<>(GreetingUiState.idle());

    public GreetingViewModel(GreeterRepository repository, Executor backgroundExecutor) {
        this.repository = repository;
        this.backgroundExecutor = backgroundExecutor;
    }

    public LiveData<GreetingUiState> getUiState() {
        return uiState;
    }

    public void onGreetClicked(String rawName) {
        GreetingUiState current = uiState.getValue();
        if (current != null && current.loading) {
            return; // ignore taps while a request is in flight
        }
        String name = rawName == null ? "" : rawName.trim();
        if (name.isEmpty()) {
            uiState.setValue(GreetingUiState.error(EMPTY_NAME_ERROR));
            return;
        }
        uiState.setValue(GreetingUiState.loading());
        backgroundExecutor.execute(() -> {
            try {
                uiState.postValue(GreetingUiState.success(repository.sayHello(name)));
            } catch (GreeterException e) {
                uiState.postValue(GreetingUiState.error(e.getMessage()));
            } catch (RuntimeException e) {
                uiState.postValue(GreetingUiState.error("Something went wrong"));
            }
        });
    }

    // ---- Manual dependency wiring (swap for Hilt/Koin if you prefer) ----

    private static final Executor IO = Executors.newSingleThreadExecutor();

    public static ViewModelProvider.Factory factory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                ManagedChannel channel = OkHttpChannelBuilder
                        .forAddress(BuildConfig.GRPC_HOST, BuildConfig.GRPC_PORT)
                        .usePlaintext()
                        .build();
                return (T) new GreetingViewModel(new GrpcGreeterRepository(channel), IO);
            }
        };
    }
}
