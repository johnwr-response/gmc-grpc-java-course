package no.responseweb.gmc.grpc.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC Client");
        CalculatorClient main = new CalculatorClient();
        main.run();
    }
    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();
        // doUnaryCall(channel);
        // doServerStreamingCall(channel);
        // doClientStreamingCall(channel);
        // doBiDiStreamingCall(channel);
        doErrorCall(channel);
        System.out.println("Shutting down channel");
        channel.shutdown();
    }
    private void doUnaryCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        var sumRequest = SumRequest.newBuilder().setFirstNumber(3).setSecondNumber(10).build();
        var sumResponse = stub.sum(sumRequest);
        System.out.println(sumRequest.getFirstNumber() + " + " + sumRequest.getSecondNumber() + " = " + sumResponse.getSumResult());
    }
    private void doServerStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        var number = 120;
        stub.primeNumberDecomposition(PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(number)
                .build())
                .forEachRemaining(primeNumberDecompositionResponse ->
                        System.out.println("Prime factor: " + primeNumberDecompositionResponse.getPrimeFactor())
                );
    }
    private void doClientStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        var latch = new CountDownLatch(1);
        StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Received a response from the server");
                System.out.println("Average = " + value.getAverage());
            }

            @Override
            public void onError(Throwable t) {
                //
            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us data");
                latch.countDown();

            }
        });

        for (var i = 1; i < 5 ; i++) {
            requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(i).build());
        }
        requestObserver.onCompleted();

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void doBiDiStreamingCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        var latch = new CountDownLatch(1);
        StreamObserver<FindMaximumRequest> requestObserver = asyncClient.findMaximum(new StreamObserver<FindMaximumResponse>() {
            @Override
            public void onNext(FindMaximumResponse value) {
                System.out.println("Got new maximum from the server: " + value.getMaximum());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us data");

            }
        });

        Arrays.asList(3,5,17,9,8,30,12).forEach(number -> {
            System.out.println("Sending number: " + number);
            requestObserver.onNext(FindMaximumRequest.newBuilder().setNumber(number).build());
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        requestObserver.onCompleted();

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void doErrorCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);
        int number = -1;
        try {
            blockingStub.squareRoot(SquareRootRequest.newBuilder().setNumber(number).build());
        } catch (StatusRuntimeException e) {
            System.out.println("Got an exception for square root!");
            e.printStackTrace();
        }
    }

}
