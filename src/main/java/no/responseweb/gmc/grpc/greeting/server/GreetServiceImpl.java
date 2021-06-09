package no.responseweb.gmc.grpc.greeting.server;

import com.proto.dummy.GreetRequest;
import com.proto.dummy.GreetResponse;
import com.proto.dummy.GreetServiceGrpc;
import com.proto.dummy.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        // Extract the fields we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        // Create the response
        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResponse(result)
                .build();

        // Send the response
        responseObserver.onNext(response);

        // Complete the RPC Call
        responseObserver.onCompleted();
    }
}
