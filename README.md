# Android Challenge

Interview starter. Stack matches the main app: Java 17, ViewBinding, AGP 8.12.3, Gradle 8.14.5,
Kotlin plugin 2.2.0, compileSdk 36 / minSdk 33, protobuf 3.25.1 + gRPC-lite 1.82.2 (okhttp).

## Task
Call `Greeter.SayHello` (see `app/src/main/proto/hello.proto`) and show the reply in the app:
a button that sends a name, and a text view that shows "Hello, <name>!". Handle loading and errors.
Structure it however you think is right (ViewModel + repository is welcome).

## Run
1. Start the server: `./gradlew :server:run` (plaintext, port 50051)
2. Run the `app` on an emulator. The emulator reaches the host at `10.0.2.2:50051`.

Generated gRPC classes (`GreeterGrpc`, `HelloRequest`, `HelloReply`) live in
`com.example.androidchallenge.grpc`.
