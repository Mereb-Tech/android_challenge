package com.example.androidchallenge.data;

import io.grpc.ManagedChannel;

/**
 * TODO (candidate): implement the gRPC call.
 *
 * <ul>
 *   <li>Use the generated {@code GreeterGrpc} blocking stub with a {@code HelloRequest}.</li>
 *   <li>Add a deadline to the call.</li>
 *   <li>Translate {@code StatusRuntimeException} into {@link GreeterException} with a user-friendly message.</li>
 *   <li>Decide who owns and shuts down the {@link ManagedChannel}.</li>
 * </ul>
 *
 * See {@code GrpcGreeterRepositoryTest} (currently @Ignore'd) for the expected behaviour.
 */
public class GrpcGreeterRepository implements GreeterRepository {

    private final ManagedChannel channel;

    public GrpcGreeterRepository(ManagedChannel channel) {
        this.channel = channel;
    }

    @Override
    public String sayHello(String name) throws GreeterException {
        throw new UnsupportedOperationException("TODO: implement GrpcGreeterRepository.sayHello");
    }
}
