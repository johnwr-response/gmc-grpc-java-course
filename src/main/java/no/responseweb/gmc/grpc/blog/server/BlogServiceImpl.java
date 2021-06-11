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
                            .withDescription("The blog with the corresponding id was not found")
                            .augmentDescription(e.getLocalizedMessage())
                            .asRuntimeException()
            );
        }
        if (result == null) {
            log.info("Blog not found");
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with the corresponding id was not found")
                            .asRuntimeException()
            );
        } else {
            log.info("Blog found, sending response");
            var blog = documentToBlog(result);
            responseObserver.onNext(ReadBlogResponse.newBuilder().setBlog(blog).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {
        log.info("Received Update Blog Request");
        var blog = request.getBlog();
        var blogId = blog.getId();
        log.info("Searching for a blog so we can update it");
        Document result = null;
        try {
            result = collection.find(Filters.eq("_id", new ObjectId(blogId))).first();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with the corresponding id was not found")
                            .augmentDescription(e.getLocalizedMessage())
                            .asRuntimeException()
            );
        }
        if (result == null) {
            log.info("Blog not found");
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with the corresponding id was not found")
                            .asRuntimeException()
            );
        } else {
            Document replacement = new Document("author_id", blog.getAuthorId())
                    .append("title", blog.getTitle())
                    .append("content", blog.getContent())
                    ;
            log.info("Replacing blog in database");
            collection.replaceOne(Filters.eq("_id", result.getObjectId("_id")), replacement);
            log.info("Replaced! Sending blog as response");
            responseObserver.onNext(UpdateBlogResponse.newBuilder().setBlog(documentToBlog(replacement)).build());
            responseObserver.onCompleted();
        }
    }

    private Blog documentToBlog(Document document) {
        return Blog.newBuilder()
                .setAuthorId(document.getString("author_id"))
                .setTitle(document.getString("title"))
                .setContent(document.getString("content"))
                .setId(document.getObjectId("_id").toString())
                .build();
    }
}
