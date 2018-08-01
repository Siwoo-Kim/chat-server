package com.siwoo.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReciever extends Thread{
    Socket socket;
    DataInputStream request;

    public ClientReciever(Socket socket) {
        this.socket = socket;
        try {
            request = new DataInputStream(socket.getInputStream());
        }catch (IOException e) {
            //Ignore
        }
    }

    public void run() {
        while (request != null) {
            try {
                System.out.println(request.readUTF());
            }catch (IOException e) {

            }
        }
    }

}
