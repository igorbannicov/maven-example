import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Register a simple handler
        server.createContext("/", new MyHandler());

        // Start the server
        server.setExecutor(null); // use default executor
        System.out.println("Server running at http://localhost:" + port);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body><h1>Hello from Java HTTP Server!</h1></body></html>";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
