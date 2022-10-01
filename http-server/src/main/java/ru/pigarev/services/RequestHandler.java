package ru.pigarev.services;

import ru.pigarev.logger.ConsoleLogger;
import ru.pigarev.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RequestHandler implements Runnable {

    private static final String WWW = "F:\\JavaProjects\\webserver\\www";

    private static final Logger logger = new ConsoleLogger();

    private final SocketService socketService;

    public RequestHandler(SocketService socketService) {
        this.socketService = socketService;
    }

    @Override
    public void run() {

        List<String> request = socketService.readRequest();
        String headers;

        String[] parts = request.get(0).split(" ");

        Path path = Paths.get(WWW, parts[1]);
        if (!Files.exists(path)) {
            headers = "HTTP/1.1 404 NOT_FOUND\n" +
                    "Content-Type: text/html; charset=utf-8\n\n";

            socketService.writeResponse(headers,
                    new BufferedReader(new StringReader("<h1>Файл не найден!</h1>\n")));

        } else {
            headers = "HTTP/1.1 200 OK\n" +
                    "Content-Type: text/html; charset=utf-8\n\n";

            try {
                socketService.writeResponse(headers,
                        Files.newBufferedReader(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info(String.format("Client disconnected with response: %s",
                headers));
    }
}
