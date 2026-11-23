package com.example.androidchallenge.data;

/**
 * Data-layer contract. The ViewModel depends on this interface only, so it can be
 * unit tested with a fake and the gRPC details stay replaceable.
 */
public interface GreeterRepository {

    /**
     * Blocking call: must be invoked off the main thread.
     *
     * @return the greeting text, e.g. "Hello, Ada!"
     * @throws GreeterException when the server cannot be reached or returns an error
     */
    String sayHello(String name) throws GreeterException;
}
