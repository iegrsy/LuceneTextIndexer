import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class LuceneIndexerServer {
    private static final Logger logger = Logger.getLogger(LuceneIndexerServer.class.getName());

    private final int port;
    private final Server server;

    /**
     * Create a RouteGuide server using serverBuilder as a base and features as data.
     */
    public LuceneIndexerServer(int port) {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(new LuceneSEServiceImpl())
                .build();
    }

    /**
     * Start serving requests.
     */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    LuceneIndexerServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}

