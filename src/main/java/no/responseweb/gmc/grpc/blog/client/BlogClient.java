package no.responseweb.gmc.grpc.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlogClient {
    @SuppressWarnings("FieldCanBeLocal")
    private static final boolean SEND_ERROR = false;
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

        var blogId = createResponse.getBlog().getId();
        log.info("Reading blog...");
        var readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder().setBlogId(blogId).build());
        log.info(readBlogResponse.toString());

        if (SEND_ERROR) {
            log.info("Reading blog with non existing id...");
            var readBlogResponseNotFound = blogClient.readBlog(ReadBlogRequest.newBuilder().setBlogId("60c23244b126be18750b6f21").build());
            log.info(readBlogResponseNotFound.toString());
        }

        var newBlog = Blog.newBuilder()
                .setId(blogId)
                .setAuthorId("Changed Author")
                .setTitle("New blog! (updated)")
                .setContent("Hello world, this is my first blog! I've added some more content.")
                .build();

        log.info("Updating blog...");
        var updateBlogResponse = blogClient.updateBlog(UpdateBlogRequest.newBuilder().setBlog(newBlog).build());
        log.info("Update blog");
        log.info(updateBlogResponse.toString());


    }
}
