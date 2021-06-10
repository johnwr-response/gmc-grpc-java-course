package no.responseweb.gmc.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

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
}
