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
