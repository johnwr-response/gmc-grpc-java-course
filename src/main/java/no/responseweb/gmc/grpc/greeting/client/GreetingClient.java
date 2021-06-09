package no.responseweb.gmc.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC Client");

        GreetingClient main = new GreetingClient();
        main.run();
    }
    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // If needed, forces deactivation of SSL during development. NOT to be used in production!!!
                .build();
        // doUnaryCall(channel);
        doServerStreamingCall(channel);
        System.out.println("Shutting down channel");
        channel.shutdown();
    }
    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("John")
                .setLastName("Wr")
                .build();
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        System.out.println("Greeting Response: " + greetResponse.getResponse() );
    }
    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("John").build()).build();
        greetClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(greetManyTimesResponse -> {
            System.out.println("Response: " + greetManyTimesResponse.getResponse());
        });
    }
}
