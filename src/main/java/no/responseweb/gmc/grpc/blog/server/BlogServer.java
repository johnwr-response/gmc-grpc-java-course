package no.responseweb.gmc.grpc.blog.server;

import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BlogServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(50053)
                .addService(new BlogServiceImpl())
                .addService(ProtoReflectionService.newInstance()) // reflection
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Received Shutdown Request");
            server.shutdown();
            log.info("Successfully stopped the server");
        }));

        server.awaitTermination();
    }
}
