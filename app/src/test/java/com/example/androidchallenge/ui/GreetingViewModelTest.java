package com.example.androidchallenge.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.androidchallenge.data.GreeterException;
import com.example.androidchallenge.data.GreeterRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

public class GreetingViewModelTest {

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    /** Holds submitted work so a test can inspect the "loading" state before it runs. */
    private static class ManualExecutor implements Executor {
        final Queue<Runnable> tasks = new ArrayDeque<>();
        @Override public void execute(Runnable r) { tasks.add(r); }
        void runAll() { while (!tasks.isEmpty()) tasks.poll().run(); }
    }

    private static class FakeRepository implements GreeterRepository {
        int calls;
        String lastName;
        GreeterException failure;
        RuntimeException crash;

        @Override
        public String sayHello(String name) throws GreeterException {
            calls++;
            lastName = name;
            if (failure != null) throw failure;
            if (crash != null) throw crash;
            return "Hello, " + name + "!";
        }
    }

    private FakeRepository repository;
    private ManualExecutor executor;
    private GreetingViewModel viewModel;

    @Before
    public void setUp() {
        repository = new FakeRepository();
        executor = new ManualExecutor();
        viewModel = new GreetingViewModel(repository, executor);
    }

    @Test
    public void initialState_isIdle() {
        GreetingUiState s = viewModel.getUiState().getValue();
        assertFalse(s.loading);
        assertNull(s.message);
        assertNull(s.error);
    }

    @Test
    public void greet_showsLoading_thenSuccess() {
        viewModel.onGreetClicked("Ada");
        assertTrue(viewModel.getUiState().getValue().loading);

        executor.runAll();

        GreetingUiState s = viewModel.getUiState().getValue();
        assertFalse(s.loading);
        assertEquals("Hello, Ada!", s.message);
        assertNull(s.error);
    }

    @Test
    public void greet_trimsName() {
        viewModel.onGreetClicked("  Ada  ");
        executor.runAll();
        assertEquals("Ada", repository.lastName);
    }

    @Test
    public void greet_blankName_showsErrorAndSkipsRepository() {
        viewModel.onGreetClicked("   ");
        executor.runAll();

        assertEquals(GreetingViewModel.EMPTY_NAME_ERROR, viewModel.getUiState().getValue().error);
        assertEquals(0, repository.calls);
    }

    @Test
    public void greet_nullName_isTreatedAsBlank() {
        viewModel.onGreetClicked(null);
        assertEquals(GreetingViewModel.EMPTY_NAME_ERROR, viewModel.getUiState().getValue().error);
    }

    @Test
    public void greet_repositoryFailure_showsErrorMessage() {
        repository.failure = new GreeterException("Server unavailable", null);

        viewModel.onGreetClicked("Ada");
        executor.runAll();

        GreetingUiState s = viewModel.getUiState().getValue();
        assertFalse(s.loading);
        assertEquals("Server unavailable", s.error);
        assertNull(s.message);
    }

    @Test
    public void greet_unexpectedCrash_doesNotLeaveLoadingStuck() {
        repository.crash = new IllegalStateException("boom");

        viewModel.onGreetClicked("Ada");
        executor.runAll();

        GreetingUiState s = viewModel.getUiState().getValue();
        assertFalse(s.loading);
        assertEquals("Something went wrong", s.error);
    }

    @Test
    public void greet_whileLoading_isIgnored() {
        viewModel.onGreetClicked("Ada");
        viewModel.onGreetClicked("Grace");
        executor.runAll();

        assertEquals(1, repository.calls);
        assertEquals("Ada", repository.lastName);
    }

    @Test
    public void greet_afterError_canRetry() {
        repository.failure = new GreeterException("Server unavailable", null);
        viewModel.onGreetClicked("Ada");
        executor.runAll();

        repository.failure = null;
        viewModel.onGreetClicked("Ada");
        executor.runAll();

        assertEquals("Hello, Ada!", viewModel.getUiState().getValue().message);
    }
}
