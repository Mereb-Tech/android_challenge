package com.example.androidchallenge.ui;

import androidx.annotation.Nullable;

/** Immutable snapshot of everything the screen needs to render. */
public final class GreetingUiState {

    public final boolean loading;
    @Nullable public final String message;
    @Nullable public final String error;

    private GreetingUiState(boolean loading, @Nullable String message, @Nullable String error) {
        this.loading = loading;
        this.message = message;
        this.error = error;
    }

    public static GreetingUiState idle() {
        return new GreetingUiState(false, null, null);
    }

    public static GreetingUiState loading() {
        return new GreetingUiState(true, null, null);
    }

    public static GreetingUiState success(String message) {
        return new GreetingUiState(false, message, null);
    }

    public static GreetingUiState error(String error) {
        return new GreetingUiState(false, null, error);
    }
}
