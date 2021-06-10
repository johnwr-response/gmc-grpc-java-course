package no.responseweb.gmc.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

@Slf4j
public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private final MongoClient mongoClient = MongoClients.create("mongodb://root:example@localhost:27017");
    private final MongoDatabase database = mongoClient.getDatabase("myDb");
    private final MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
        log.info("Received Create Blog Request");
        var blog = request.getBlog();
        Document doc = new Document("author_id", blog.getAuthorId())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());
        log.info("Inserting Blog... ");
        collection.insertOne(doc);
        var id = doc.getObjectId("_id").toString();
        log.info("Inserted Blog {} ", id);

        CreateBlogResponse response = CreateBlogResponse.newBuilder().setBlog(
            blog.toBuilder().setId(id).build()
        ).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
        log.info("Received Read Blog Request");
        var blogId = request.getBlogId();
        log.info("Searching for a blog");
        Document result = null;
        try {
            result = collection.find(Filters.eq("_id", new ObjectId(blogId))).first();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with the correspondnig id was not found")
                            .augmentDescription(e.getLocalizedMessage())
                            .asRuntimeException()
            );
        }
        if (result == null) {
            log.info("Blog not found");
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with the correspondnig id was not found")
                            .asRuntimeException()
            );
        } else {
            log.info("Blog found, sending response");
            var blog = Blog.newBuilder()
                    .setAuthorId(result.getString("author_id"))
                    .setTitle(result.getString("title"))
                    .setContent(result.getString("content"))
                    .setId(blogId)
                    .build();
            responseObserver.onNext(ReadBlogResponse.newBuilder().setBlog(blog).build());
            responseObserver.onCompleted();
        }
    }
}
