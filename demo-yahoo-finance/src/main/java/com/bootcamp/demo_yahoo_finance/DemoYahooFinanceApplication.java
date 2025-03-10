package com.bootcamp.demo_yahoo_finance;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class DemoYahooFinanceApplication {
    public static void main(String[] args) throws IOException {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

        CookieManager cookieManager = new CookieManager(userAgent);
        CrumbManager crumbManager = new CrumbManager(cookieManager, userAgent);
        YahooFinanceManager yahooFinanceManager = new YahooFinanceManager(crumbManager, cookieManager, userAgent);

        // Create an HTTP server instance, specifying that the server will listen on port 8101
        // Backlog controls the number of queued incoming connections. A value of 0 means the system will use a default queue size.
        HttpServer server = HttpServer.create(new InetSocketAddress(8101), 0);

        // Register a new HTTP endpoint (/crumb)
        // exchange -> { ... } is a lambda function that handles requests sent to /crumb.
        // The exchange object represents an HTTP request/response.
        // It provides access to request details and allows sending responses.
        server.createContext("/crumb", exchange -> {
            // Get the latest crumb key
            String response = crumbManager.getCrumb();
            // sendResponseHeaders(int rCode, long responseLength)
            // rCode - the response code to send
            // 200 is the HTTP status code (200 OK means the request was successful).
            // responseLength - if > 0, specifies a fixed response body length and that exact number of bytes must be written to the stream acquired from getResponseBody(),
            // or else if equal to 0, then chunked encoding is used, and an arbitrary number of bytes may be written.
            // if <= -1, then no response body length is specified and no response body may be written.
            exchange.sendResponseHeaders(200, response.length());
            // sendResponseHeaders(int,long)) must be called prior to calling getResponseBody()
            // Get the output stream to write the response.
            OutputStream os = exchange.getResponseBody();
            // Convert the string into bytes and send the byte array as the HTTP response body
            os.write(response.getBytes());
            // Ensure that the stream is properly closed
            os.close();
        });

        // 1. Register the endpoint /quote
        server.createContext("/quote", exchange -> {
            // 2. Extract the Query Parameter (symbol)
            // getRequestURI() retrieves the full request URI
            // .getQuery() extracts just the query string (symbol=AAPL)
            String query = exchange.getRequestURI().getQuery();
            // 3. Validate the Query Parameter
            // Checks if query starts with "symbol="
            // Ensures the query string contains a valid symbol parameter
            // Prevents invalid requests
            if (query == null || !query.startsWith("symbol=")) {
                String errorResponse = "Missing 'symbol' parameter";
                exchange.sendResponseHeaders(400, errorResponse.length());
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
                return;
            }

            // 4. Extract the Symbol
            // Now symbol holds the stock ticker.
            String symbol = query.replace("symbol=", "").trim();
            // 5. Fetch Stock Quote
            String quoteJson = yahooFinanceManager.quote(symbol);

            // 6. Send the Response
            exchange.sendResponseHeaders(200, quoteJson.length());
            OutputStream os = exchange.getResponseBody();
            os.write(quoteJson.getBytes());
            os.close();
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8101");
    }
}


