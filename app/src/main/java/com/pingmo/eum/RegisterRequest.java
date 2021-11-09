package com.pingmo.eum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class RegisterRequest {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public RegisterRequest(String ip, int port, String query) {

        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);

                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pw = new PrintWriter(socket.getOutputStream());

                    byte[] bytes = query.getBytes(StandardCharsets.UTF_8);
                    pw.println(bytes);
                    pw.flush();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                        if (br != null) {
                            br.close();
                        }
                        if (pw != null) {
                            pw.close();
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }.start();
    }
}
