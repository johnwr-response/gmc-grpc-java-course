package no.responseweb.gmc.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        var sumResponse = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber() + request.getSecondNumber())
                .build();

        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        var number = request.getNumber();
        var divisor = 2;
        while (number>1) {
            if (number % divisor == 0) {
                number = number / divisor;
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder().setPrimeFactor(divisor).build());
            } else {
                divisor = divisor + 1;
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        return new StreamObserver<>() {

            int sum = 0;
            int count = 0;

            @Override
            public void onNext(ComputeAverageRequest value) {
                sum += value.getNumber();
                count += 1;
            }

            @Override
            public void onError(Throwable t) {
                //
            }

            @Override
            public void onCompleted() {
                double average = (double) sum / count;
                responseObserver.onNext(ComputeAverageResponse.newBuilder().setAverage(average).build());
                responseObserver.onCompleted();
            }
        };
    }
}
