import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        int lifetimeSeconds = 30;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler(lifetimeSeconds));
        server.setExecutor(Executors.newCachedThreadPool());

        System.out.println("Server running at http://localhost:" + port);

        server.start();

        // Stop server after 60 seconds
        new Thread(() -> {
            try {
                Thread.sleep(lifetimeSeconds * 1000L);
                System.out.println("Shutting down server after " + lifetimeSeconds + " seconds...");
                server.stop(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class MyHandler implements HttpHandler {
        private final int seconds;

        public MyHandler(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String buildNumber = System.getProperty("buildNumber", "unknown");
            String response = "<!DOCTYPE html><html><head><title>Temp Server</title>"
                    + "<script>"
                    + "let seconds = " + seconds + ";"
                    + "setInterval(() => {"
                    + "  if (seconds <= 0) return;"
                    + "  document.getElementById('counter').textContent = --seconds;"
                    + "}, 1000);"
                    + "</script></head><body>"
                    + "<h1>This Java server will stop in <span id='counter'>" + seconds + "</span> seconds.</h1>"
                    + "<h2>Build #" + buildNumber + "</h2>"
                    + "</body></html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
