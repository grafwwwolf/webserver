package ru.pigarev.services;

import ru.pigarev.logger.ConsoleLogger;
import ru.pigarev.logger.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SocketService implements Closeable {

    private final Socket socket;

    private static final Logger logger = new ConsoleLogger();

    public SocketService(Socket socket) {
        this.socket = socket;
    }

    public List<String> readRequest() {

        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(), StandardCharsets.UTF_8));

            while (!input.ready()) ;

            List<String> request = new ArrayList<>();
            while (input.ready()) {

                String line = input.readLine();
                logger.info(line);
                request.add(line);
            }
            return request;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeResponse(String headers, BufferedReader reader) {

        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            output.print(headers);
            reader.transferTo(output);
            output.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws IOException {

        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
