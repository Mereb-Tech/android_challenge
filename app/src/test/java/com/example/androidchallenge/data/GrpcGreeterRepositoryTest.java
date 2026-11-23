package com.example.androidchallenge.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.example.androidchallenge.grpc.GreeterGrpc;
import com.example.androidchallenge.grpc.HelloReply;
import com.example.androidchallenge.grpc.HelloRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Acceptance tests for the candidate's {@link GrpcGreeterRepository}, run against an
 * in-process server (no network). Remove the @Ignore annotations once implemented.
 */
public class GrpcGreeterRepositoryTest {

    private Server server;
    private ManagedChannel channel;
    private volatile boolean failWithUnavailable;

    @Before
    public void setUp() throws Exception {
        String name = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(name).directExecutor()
                .addService(new GreeterGrpc.GreeterImplBase() {
                    @Override
                    public void sayHello(HelloRequest req, StreamObserver<HelloReply> out) {
                        if (failWithUnavailable) {
                            out.onError(Status.UNAVAILABLE.asRuntimeException());
                            return;
                        }
                        out.onNext(HelloReply.newBuilder().setMessage("Hello, " + req.getName() + "!").build());
                        out.onCompleted();
                    }
                }).build().start();
        channel = InProcessChannelBuilder.forName(name).directExecutor().build();
    }

    @After
    public void tearDown() {
        channel.shutdownNow();
        server.shutdownNow();
    }

    @Ignore("Enable once GrpcGreeterRepository.sayHello is implemented")
    @Test
    public void sayHello_returnsServerMessage() throws Exception {
        assertEquals("Hello, Ada!", new GrpcGreeterRepository(channel).sayHello("Ada"));
    }

    @Ignore("Enable once GrpcGreeterRepository.sayHello is implemented")
    @Test
    public void sayHello_serverUnavailable_throwsGreeterException() {
        failWithUnavailable = true;
        assertThrows(GreeterException.class, () -> new GrpcGreeterRepository(channel).sayHello("Ada"));
    }
}
