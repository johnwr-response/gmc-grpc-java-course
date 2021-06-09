package no.responseweb.gmc.grpc.greeting.client;

import com.proto.dummy.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // If needed, forces deactivation of SSL during development. NOT to be used in production!!!
                .build();

        System.out.println("Creating stub ");
        // Synchronous Client:
        // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
        // Asynchronous Client:
        // DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("John")
                .setLastName("Wr")
                .build();
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        System.out.println("Greetig Response: " + greetResponse.getResponse() );

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
