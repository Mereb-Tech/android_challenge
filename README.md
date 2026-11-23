# Android Challenge

Interview starter. Stack matches the main app: Java 17, ViewBinding, AGP 8.12.3, Gradle 8.14.5,
Kotlin plugin 2.2.0, compileSdk 36 / minSdk 33, protobuf 3.25.1 + gRPC-lite 1.82.2 (okhttp).

## Task
Implement `GrpcGreeterRepository.sayHello` (`app/src/main/java/com/example/androidchallenge/data/`)
so the app can call `Greeter.SayHello` (see `app/src/main/proto/hello.proto`) and show
"Hello, <name>!" on screen. Everything else (UI, ViewModel, state, wiring) is already in place.

Until it is implemented, tapping **Say hello** shows "Something went wrong".

Requirements:
- Use the generated `GreeterGrpc` blocking stub; set a call deadline.
- Map `StatusRuntimeException` to `GreeterException` with a user-friendly message.
- Decide who owns the `ManagedChannel` and make sure it is shut down.
- Enable and pass the `@Ignore`d tests in `GrpcGreeterRepositoryTest`.

## Architecture (MVVM)

```
MainActivity (View)  --observes-->  GreetingViewModel  --calls-->  GreeterRepository (interface)
  renders GreetingUiState            holds LiveData<GreetingUiState>        ^
  forwards clicks                    runs work on a background Executor     |
                                                                      GrpcGreeterRepository  <-- TODO
```

| Layer | Class | Responsibility |
|-------|-------|----------------|
| UI | `ui/MainActivity` | Renders state, forwards input. No logic. |
| UI | `ui/GreetingUiState` | Immutable state: idle / loading / success / error. |
| UI | `ui/GreetingViewModel` | Validation, loading/error handling, survives rotation. Depends on the interface only. |
| Data | `data/GreeterRepository` | Contract the ViewModel depends on (easy to fake). |
| Data | `data/GrpcGreeterRepository` | gRPC implementation (**candidate**). |
| Data | `data/GreeterException` | Keeps gRPC types out of the UI layer. |

Dependencies are wired manually in `GreetingViewModel.factory()`. Swapping in Hilt/Koin is welcome.

## Tests
Run all unit tests: `./gradlew :app:testDebugUnitTest`

| Test | What it covers | Status |
|------|----------------|--------|
| `GreetingViewModelTest` | Initial state, loading then success, name trimming, blank/null name, repository failure, unexpected crash, taps ignored while loading, retry after error. Uses a fake repository and a manual executor. | Passing (9 tests) |
| `GrpcGreeterRepositoryTest` | Success reply and `UNAVAILABLE` error against an in-process gRPC server (no network). | `@Ignore`d: enable once implemented |

## Run
1. Use JDK 17-21 for Gradle (Gradle 8.14.5 does not support JDK 25+). In Android Studio:
   Settings > Build Tools > Gradle > Gradle JDK.
2. Start the server: `./gradlew :server:run` (plaintext, port 50051).
3. Run the `app` on an emulator. It reaches the host at `10.0.2.2:50051`
   (`GRPC_HOST` / `GRPC_PORT` in `app/build.gradle`).

Generated gRPC classes (`GreeterGrpc`, `HelloRequest`, `HelloReply`) live in
`com.example.androidchallenge.grpc`.
