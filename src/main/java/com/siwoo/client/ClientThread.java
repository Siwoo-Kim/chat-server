package com.siwoo.client;

import com.siwoo.ChatServer;
import com.siwoo.repository.ClientRepository;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread{
    Socket socket;
    DataInputStream reqeust;
    DataOutputStream response;
    ChatServer chatServer;
    ClientRepository clientRepository;

    public ClientThread(Socket socket, ChatServer server, ClientRepository repository) {
        this.socket = socket;
        try {
            reqeust = new DataInputStream(socket.getInputStream());
            response = new DataOutputStream(socket.getOutputStream());
            chatServer = server;
        }catch (IOException e) {
            System.out.println("Connecting client to server failed");
            e.printStackTrace();
        }
    }

    public void run() {
        Client client = null;

        try {
            String name = reqeust.readUTF();
            System.out.println("there: " + name);
            chatServer.emit("#" + name +" is connecting to server");
            client = clientRepository.save(new Client.Builder().name(name).build());
            chatServer.addChatter(client, response);
            System.out.println(client);
            System.out.println(reqeust);
            System.out.println("Now current number of user is " + clientRepository.size() + ".");

            while (reqeust != null) {
                chatServer.emit(reqeust.readUTF());
            }
        }catch (IOException e) {
            e.printStackTrace();
            //Ignore
        } finally {
            chatServer.emit("#"+ client.getName() +" left");
            chatServer.removeChatter(client, response);
            System.out.println("Now current number of user is " + clientRepository.size() + ".");
        }
    }
}
