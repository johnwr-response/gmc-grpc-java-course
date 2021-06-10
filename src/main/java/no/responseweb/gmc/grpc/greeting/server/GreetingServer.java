package no.responseweb.gmc.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello gRPC");

        boolean secure = false;

        // plaintext server
        Server serverPlainText = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .build();

        // secure server
        Server serverSecure = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .useTransportSecurity(
                        new File("ssl/server.crt"),
                        new File("ssl/server.pem")
                )
                .build();

        if (secure) {
            serverSecure.start();
        } else {
            serverPlainText.start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            if (secure) {
                serverSecure.shutdown();
            } else {
                serverPlainText.shutdown();
            }
            System.out.println("Successfully stopped the server");
        }));

        if (secure) {
            serverSecure.awaitTermination();
        } else {
            serverPlainText.awaitTermination();
        }
    }
}
