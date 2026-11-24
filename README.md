# Android Challenge

Fork this repo and work on your own github.

## Tech used
- **Language / UI:** Java 17, XML layouts, Material 3, AppCompat, ConstraintLayout
- **Networking:** gRPC (lite, OkHttp transport) with Protocol Buffers, compiled by the protobuf Gradle plugin
- **Jetpack:** Lifecycle ViewModel and LiveData
- **Build:** Gradle 8.14.5, Android Gradle Plugin 8.12.3, Kotlin plugin 2.2.0
- **SDK:** compileSdk 36, targetSdk 35, minSdk 33
- **Test:** JUnit 4, AndroidX Test, gRPC in-process transport
- **Server (provided):** Java gRPC server in `server/`

## Task
The app should call the `Greeter.SayHello` gRPC service (`app/src/main/proto/hello.proto`) and show the
greeting it returns on screen.

## Expectations
- Use MVVM and modern Android best practices.
- Handle the cases a real user would hit.
- Write the tests you think are necessary.

## Run
1. Use JDK 17-21 for Gradle (Gradle 8.14.5 does not support JDK 25+).
2. Start the server: `./gradlew :server:run` (plaintext, port 50051).
3. Run the `app` on an emulator. It reaches the host at `10.0.2.2:50051`.
