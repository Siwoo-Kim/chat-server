package com.siwoo.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender extends Thread {
    Socket socket;
    DataOutputStream response;
    String name;

    public ClientSender(Socket socket, String name) {
        this.socket = socket;
        try {
            response = new DataOutputStream(socket.getOutputStream());
            this.name = name;
        } catch (IOException e) {
            //Ignore
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            if(response != null) {
                response.writeUTF(name);
            }

            while (response != null) {
                response.writeUTF("[" + name +"]" + scanner.nextLine());
            }
        }catch (IOException e) {
            //Ignore
        }
    }
}
