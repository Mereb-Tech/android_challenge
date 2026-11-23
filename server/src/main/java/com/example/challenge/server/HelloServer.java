package com.example.challenge.server;

import com.example.androidchallenge.grpc.GreeterGrpc;
import com.example.androidchallenge.grpc.HelloReply;
import com.example.androidchallenge.grpc.HelloRequest;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/** Minimal Greeter server for the interview. Run with: ./gradlew :server:run */
public class HelloServer {

    private static final int PORT = 50051;

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            String name = req.getName().isEmpty() ? "World" : req.getName();
            responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello, " + name + "!").build());
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(PORT).addService(new GreeterImpl()).build().start();
        System.out.println("Greeter server listening on " + PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
