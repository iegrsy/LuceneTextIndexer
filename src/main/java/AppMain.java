public class AppMain {
    public static void main(String[] args) throws Exception {
        LuceneIndexerServer server = new LuceneIndexerServer(8980);
        server.start();
        server.blockUntilShutdown();
    }
}
