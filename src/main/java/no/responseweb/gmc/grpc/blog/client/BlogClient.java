package no.responseweb.gmc.grpc.blog.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlogClient {
    public static void main(String[] args) {
        log.info("Hello I'm a gRPC Client for Blog");
        var main = new BlogClient();
        main.run();
    }
    private void run() {

    }
}
