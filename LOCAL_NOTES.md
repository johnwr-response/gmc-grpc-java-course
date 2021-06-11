# gRPC [Java] Master Class: Build Modern API & Micro services

### Section 3: [Theory] gRPC Internals Deep Dive

#### Protocol Buffers & Language Interoperability
- gRPC deep dive
    - protocol buffers. Used to define the
        - messages (data, request, response)
        - service (service name and RPC endpoints)
    - We then generate the code from it
- Efficiency of protocol buffers vs json
    - payload size is much less, so we save a lot of bandwidth
    - much less cpu intensive because it is binary and not human-readable
- gRPC can be used by any language
- Why protocol buffers (summary)
    - Easy to write message definition
    - The definition of the API is independent of the implementation
    - All necessary code will be written for you from a simple .proto file
    - The payload is binary, very efficient
    - Define rules to make API evolve
#### HTTP/2
- How HTTP/2 works
    - Supports multiplexing
        - The client and server can push messages in parallel over the same connection
    - Supports server push
        -  The server can push streams (multiple messages) for one request from the client
    - Supports header compression
    - Is binary
    - Is secure (SSL not required but recommended by default)
- HTTP/2 in Summary
    - Less chatter
    - More efficient protocol (less bandwidth)
    - Reduced latency
    - Increased Security
- Out of the box using gRPC framework
#### 4 Types of gRPC APIs
- Unary API
    - Classic request/response
    - Same as traditional API but with HTTP/2
- Server Streaming
    - Server will send stream of data responses based on one request
- Client Streaming
    - Server will send response after stream of data requests
- Bi Directional Streaming
    - Server will send stream of data responses based on stream of data requests
#### Scalability in gRPC
- gRPC-servers are asynchronous by default
- gRPC-clients can be both synchronous and asynchronous
- gRPC-clients can perform client side load balancing
#### Security in gRPC (SSL)
- strongly advocates ssl
- can provide authentication with interceptors
#### gRPC vs REST
- gRPC
    - (Protocol Buffers) Binary, faster, smaller
    - HTTP/2 (Lower latency)
    - Bidirectional and async
    - Stream support
    - API oriented on the "what" (no constraints, free design)
    - Code generation directly through protocol buffers
    - RPC based (built-in plumbing)
- REST
    - (JSON) Text based, slower, bigger
    - HTTP/1.1 (Higher latency)
    - client => server requests only
    - Request / Response only
    - CRUD oriented
    - Code generation through add-on (OpenAPI)
    - HTTP verb based (3rd party or self written plumbing)
- gRPC has been measured to give 25 times more performance than REST
#### Section Summary – why use gRPC
- Easy code definition in more than 11 languages
- Modern, low latency HTTP/2 transport mechanism
- SSL Security is built-in
- Support from streaming APIs for maximum performance
- gRPC is API oriented and not resource oriented (like REST)

### Section 4: [Hands-On] gRPC Project Overview & Setup
#### Java Gradle Project Setup
#### Dummy Service Code Generation
  - Run gradle task other:generateProto. Make sure the files has been generated under build/generated
#### Server Setup Boilerplate Code
#### Client Setup Boilerplate Code

### Section 5: [Hands-On] gRPC Unary
#### What’s a Unary API?
- Unary RPC calls are basic request/response. The client sends one message, and the server responds with one message
  - Very well suited when data is small
  - The recommended approach is to start with unary, and expand to streaming API when performance is an issue
- Define Unary calls using Protocol Buffers
  - Define both the request, and the response messages 
#### Greet API Definition
#### Unary API Server Implementation
#### Unary API Client Implementation
#### [Exercise/Solution] Sum API

### Section 6: [Hands-On] gRPC Server Streaming
#### What's a Server Streaming API?
- Server Streaming RPC is a new kind of API enabled by HTTP/2. 
    - The client sends one message, and the server responds with a stream of messages. Possibly infinite.
    - Well suited for 
        - when the server needs to send a lot of data (big data)
        - when the server needs to push data to clients without the need for requests (live feeds, chats, etc)
- Defined by the keyword stream
  - Define both the request, and the response messages, but with the keyword in the response 
#### GreetManyTimes API Definition
#### Server Streaming API Server Implementation
#### Server Streaming API Client Implementation
#### [Exercise/Solution] PrimeNumberDecomposition API

### Section 7: [Hands-On] gRPC Client Streaming
#### What's a Client Streaming API?
- Client Streaming RPC is a new kind of API enabled by HTTP/2. 
  - The client sends many messages, and the server responds with one messages.
    - It is up to the server when to send response back.
    - Well suited for
      - when the client needs to send a lot of data (big data)
      - when the server processing is expensive and should happen as the client sends data (can start immediately once the client starts sending the data)
      - when the client needs to push data to clients without the need for response
- Defined by the keyword stream
  - Define both the request, and the response messages, but with the keyword in the request
#### LongGreet API Definition
#### Client Streaming API Server Implementation
#### Client Streaming API Client Implementation - Part 1 - Refactoring
#### Client Streaming API Client Implementation - Part 2 - Implementation
#### [Exercise/Solution] ComputeAverage API

##### Section 8: [Hands-On] gRPC Bi-Directional Streaming
#### What's a Bi-Directional Streaming API?
- Bi Directional (bi-di) Streaming RPC is a new kind of API enabled by HTTP/2. 
  - The client sends many messages, and the server responds with many messages. The number does not have to match.
    - Well suited for
      - when the client and the server needs to send a lot of data asynchronously
      - "Chat/Gossip" protocol
      - For long term connections
- Defined by the keyword stream
  - Define both the request, and the response messages, but with the keyword in both the request and the response
#### GreetEveryone API Definition
#### Bi-Directional Streaming API Server Implementation
#### Bi-Directional Streaming API Client Implementation
#### [Exercise/Solution] FindMaximum API

### Section 9: [Hands-On] gRPC Advanced Features Deep
#### [Theory] Errors in gRPC
- Error codes
  - [https://grpc.io/docs/guides/error/](https://grpc.io/docs/guides/error/)
  - [https://avi.im/grpc-errors/](https://avi.im/grpc-errors/)
  - Use metadata context to return extra information on top of an error code
#### [Hands-On] Errors implementation
#### [Theory] Deadlines
- Deadlines allows clients to specify how long they are willing to wait for the RPC to complete
  - When the deadline triggers, the RPC is terminated with the error DEADLINE_EXCEEDED
- The gRPC documentation recommends setting a deadline for all client RPC calls 
- Server should check if the deadline has been exceeded and cancel any remaining work.
- Deadlines in Depth [https://grpc.io/blog/deadlines/](https://grpc.io/blog/deadlines/)
- Deadlines propagates across chained gRPC calls
  - This means that calls made to a microservice will propagate the deadline to all chained calls to other microservices 
#### [Hands-On] Deadlines
#### [Theory] SSL Security
- All gRPC calls in production should be running with encryption using SSL certificates. This is the default behaviour.
- gRPC support both 1-way verification (Encryption) and 2-way verification (Authentication)
#### [Hands-On] SSL Security
- [https://github.com/grpc/grpc-java/blob/master/SECURITY.md](https://github.com/grpc/grpc-java/blob/master/SECURITY.md)
- [https://grpc.io/docs/guides/auth/](https://grpc.io/docs/guides/auth/)
#### [Demo] Language Interoperability
#### gRPC Reflection & Evans CLI
- Two reasons
  - Expose which endpoints are available
  - Allow cli's to talk to server without having a preliminary .proto file
- [gRPC Server Reflection Tutorial](https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md)
- [Evans gRPC CLI](https://github.com/ktr0731/evans)
- Command: `evans repl -r -p 50052` to start using evans
- See log for Evans session below

### Section 10: [Hands-On] CRUD API with MongoDB
#### Install MongoDB
#### Install MongoDB UI - Robo 3T
#### Blog Service Java Setup
#### CreateBlog Server
#### CreateBlog Client
#### ReadBlog Server
#### ReadBlog Client
#### UpdateBlog Server
#### UpdateBlog Client
#### DeleteBlog Server
#### DeleteBlog Client

## Evans session log
```
PS C:\Users\John Håvard Wraalsen> evans --repl -r -p 50052

  ______
 |  ____|
 | |__    __   __   __ _   _ __    ___
 |  __|   ' ' / /  / _. | | '_ '  / __|
 | |____   ' V /  | (_| | | | | | '__ ,
 |______|   '_/    '__,_| |_| |_| |___/

 more expressive universal gRPC client


greet.CalculatorService@127.0.0.1:50052> show package
+-------------------------+
|         PACKAGE         |
+-------------------------+
| greet                   |
| grpc.reflection.v1alpha |
+-------------------------+

greet.CalculatorService@127.0.0.1:50052> package greet

greet@127.0.0.1:50052> show package
+-------------------------+
|         PACKAGE         |
+-------------------------+
| greet                   |
| grpc.reflection.v1alpha |
+-------------------------+

greet@127.0.0.1:50052> show service
+-------------------+--------------------------+---------------------------------+----------------------------------+
|      SERVICE      |           RPC            |          REQUEST TYPE           |          RESPONSE TYPE           |
+-------------------+--------------------------+---------------------------------+----------------------------------+
| CalculatorService | Sum                      | SumRequest                      | SumResponse                      |
| CalculatorService | PrimeNumberDecomposition | PrimeNumberDecompositionRequest | PrimeNumberDecompositionResponse |
| CalculatorService | ComputeAverage           | ComputeAverageRequest           | ComputeAverageResponse           |
| CalculatorService | FindMaximum              | FindMaximumRequest              | FindMaximumResponse              |
| CalculatorService | SquareRoot               | SquareRootRequest               | SquareRootResponse               |
+-------------------+--------------------------+---------------------------------+----------------------------------+

greet@127.0.0.1:50052> service CalculatorService

greet.CalculatorService@127.0.0.1:50052> call Sum
first_number (TYPE_INT32) => 4
second_number (TYPE_INT32) => 2
{
  "sum_result": 6
}

greet.CalculatorService@127.0.0.1:50052> call ComputeAverage
number (TYPE_INT32) => 4
number (TYPE_INT32) => 6
number (TYPE_INT32) => 8
number (TYPE_INT32) => 2
number (TYPE_INT32) =>
{
  "average": 5
}

greet.CalculatorService@127.0.0.1:50052> call FindMaximum
number (TYPE_INT32) => 4
number (TYPE_INT32) => {
  "maximum": 4
}
number (TYPE_INT32) => 6
number (TYPE_INT32) => {
  "maximum": 6
}
number (TYPE_INT32) => 3
number (TYPE_INT32) => 22
number (TYPE_INT32) => {
  "maximum : 22
}
number (TYPE_INT32) => 3
number (TYPE_INT32) => 5
number (TYPE_INT32) => 21
number (TYPE_INT32) => 23
number (TYPE_INT32) {
  "maximum": 23
}
number (TYPE_INT32) =>
{
  "maximum": 23
}

greet.CalculatorService@127.0.0.1:50052> call PrimeNumberDecomposition
number (TYPE_INT32) => 25252525252
command call: failed to set inputted values to message 'greet.PrimeNumberDecompositionRequest': failed to convert an inputted value '25252525252' to type TYPE_INT32: strconv.ParseInt: parsing "25252525252": value out of range

greet.CalculatorService@127.0.0.1:50052> call PrimeNumberDecomposition
number (TYPE_INT32) => 25554
{
  "prime_factor": 2
}
{
  "prime_factor": 3
}
{
  "prime_factor": 4259
}

greet.CalculatorService@127.0.0.1:50052> call PrimeNumberDecomposition
number (TYPE_INT32) => 256
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}
{
  "prime_factor": 2
}

greet.CalculatorService@127.0.0.1:50052>

```
