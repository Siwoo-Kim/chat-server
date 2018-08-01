package com.siwoo;

import com.siwoo.client.ClientReciever;
import com.siwoo.client.ClientSender;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();

        try {
            Socket socket = new Socket(ChatServer.HOST, ChatServer.PORT);
            System.out.println("You are connecting to server");

            Thread sender = new Thread(new ClientSender(socket, name));
            Thread reciever = new Thread(new ClientReciever(socket));

            sender.start();
            reciever.start();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
