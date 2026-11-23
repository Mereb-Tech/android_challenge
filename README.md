# Android Challenge

Interview starter. Stack matches the main app: Java 17, ViewBinding, AGP 8.12.3, Gradle 8.14.5,
Kotlin plugin 2.2.0, compileSdk 36 / minSdk 33, protobuf 3.25.1 + gRPC-lite 1.82.2 (okhttp).

## Task
Call `Greeter.SayHello` (see `app/src/main/proto/hello.proto`) from the app and show the reply on
screen: an input for a name, a button that sends it, and a text view that shows "Hello, <name>!".
The generated classes (`GreeterGrpc`, `HelloRequest`, `HelloReply`) are in
`com.example.androidchallenge.grpc`. The project starts with an empty `MainActivity`.

## Architecture requirements
Build it with **MVVM** and modern Android best practices:

- **Layers:** View (Activity, rendering only) -> ViewModel -> Repository. The ViewModel must not touch
  gRPC types or Android views, and the Activity must contain no business logic.
- **UI state:** expose one immutable UI state (idle / loading / success / error) from the ViewModel
  through `LiveData` (or `StateFlow` if you switch to Kotlin). The state must survive rotation.
- **Repository:** hide gRPC behind an interface so the ViewModel can be tested with a fake. Map gRPC
  errors (`StatusRuntimeException`) to your own error type with a user-friendly message.
- **Threading:** no network on the main thread. Never leave the UI stuck in "loading".
- **gRPC:** set a call deadline and decide who owns the `ManagedChannel` and shuts it down.
- **Dependencies:** inject them (constructor injection, a ViewModel factory, or Hilt/Koin) rather than
  creating them inside classes.
- **UX:** handle loading, empty input, and server-unavailable states. Ignore repeated taps while a
  request is in flight.

## Tests
Write the tests you think are necessary. At minimum:

- **ViewModel unit tests** (JVM, with a fake repository): initial state, loading then success, error
  path, blank input, taps ignored while loading, retry after an error.
- **Repository tests** against an in-process gRPC server (`io.grpc:grpc-inprocess`): success reply and
  a server error mapped to your own error type.

Run them with `./gradlew :app:testDebugUnitTest`. Add any test dependencies you need.

## Run
1. Use JDK 17-21 for Gradle (Gradle 8.14.5 does not support JDK 25+). In Android Studio:
   Settings > Build Tools > Gradle > Gradle JDK.
2. Start the server: `./gradlew :server:run` (plaintext, port 50051).
3. Run the `app` on an emulator. It reaches the host at `10.0.2.2:50051`.
