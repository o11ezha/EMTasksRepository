package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
    private final Socket clinetSocket;

    public Client (Socket clinetSocket) {
        this.clinetSocket = clinetSocket;
    }


    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clinetSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clinetSocket.getOutputStream(), true)) {
            String input;
            while ((input = in.readLine()) != null) {
                System.out.println("Получено от клиента: " + input);
                out.println("Сервер Вам передаёт: " + input);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
