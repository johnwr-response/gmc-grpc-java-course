package no.responseweb.gmc.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
/*
        // Unary
        SumRequest sumRequest = SumRequest.newBuilder().setFirstNumber(3).setSecondNumber(10).build();
        SumResponse sumResponse = stub.sum(sumRequest);
        System.out.println(sumRequest.getFirstNumber() + " + " + sumRequest.getSecondNumber() + " = " + sumResponse.getSumResult());
*/
        // Streaming Server
        Integer number = 120;
        stub.primeNumberDecomposition(PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(number)
                .build())
        .forEachRemaining(primeNumberDecompositionResponse -> {
            System.out.println("Prime factor: " + primeNumberDecompositionResponse.getPrimeFactor());
        });

        channel.shutdown();

    }
}
