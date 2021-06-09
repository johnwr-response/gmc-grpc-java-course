package no.responseweb.gmc.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
//                .usePlaintext() // If needed, forces deactivation of SSL during development. NOT to be used in production!!!
                .build();

        System.out.println("Creating stub ");
        // Synchronous Client:
        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
        // Asynchronous Client:
        // DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

        // Do something with the syncClient or the asyncClient

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
