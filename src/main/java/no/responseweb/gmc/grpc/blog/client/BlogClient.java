package no.responseweb.gmc.grpc.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlogClient {
    public static void main(String[] args) {
        log.info("Hello I'm a gRPC Client for Blog");
        var main = new BlogClient();
        main.run();
    }
    private void run() {
        var channel = ManagedChannelBuilder.forAddress("localhost", 50053)
                .usePlaintext()
                .build();
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        var blog = Blog.newBuilder()
                .setAuthorId("John")
                .setTitle("New blog!")
                .setContent("Hello world, this is my first blog!")
                .build();
        var createResponse = blogClient.createBlog(CreateBlogRequest.newBuilder().setBlog(blog).build());
        log.info("Received create blog response");
        log.info(createResponse.toString());
    }
}
