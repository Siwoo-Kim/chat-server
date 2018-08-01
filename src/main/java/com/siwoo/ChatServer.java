package com.siwoo;

import com.siwoo.client.Client;
import com.siwoo.client.ClientThread;
import com.siwoo.repository.ClientRepository;
import com.siwoo.repository.ClientRepositoryImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    public static final int PORT = 8080;
    public static final String HOST = "localhost";
    private ClientRepository clientRepository;
    Map clients;

    public ChatServer(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        clients = new HashMap();
        Collections.synchronizedMap(clients);
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            LocalDateTime startTime = LocalDateTime.now();
            System.out.println("server starts at " + startTime);

            while (true) {
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress()+":" + socket.getPort() +"] accesses to server");
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
            }

        } catch (IOException e) {
            System.out.println("Server got serious problem. Server down");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void emit(String message) {
        System.out.println(message);
        clientRepository
                .getClients()
                .stream()
                .forEach(client -> {
                    try {
                        DataOutputStream response = (DataOutputStream) clients.get(client);
                        response.writeUTF(message);
                    }catch (IOException e) {
                        //Ignore
                    }

                });
    }

    public static void main(String[] args) {
        new ChatServer(new ClientRepositoryImpl()).start();
    }

    public void addChatter(Client client, DataOutputStream response) {
        clients.put(client, response);
    }

    public void removeChatter(Client client, DataOutputStream response) {
        clients.remove(client);
    }


    public class ClientThread extends Thread{
        Socket socket;
        DataInputStream reqeust;
        DataOutputStream response;

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                reqeust = new DataInputStream(socket.getInputStream());
                response = new DataOutputStream(socket.getOutputStream());
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
                emit("#" + name +" is connecting to server");
                client = clientRepository.save(new Client.Builder().name(name).build());
                addChatter(client, response);
                System.out.println(client);
                System.out.println(reqeust);
                System.out.println("Now current number of user is " + clientRepository.size() + ".");

                while (reqeust != null) {
                    emit(reqeust.readUTF());
                }
            }catch (IOException e) {
                e.printStackTrace();
                //Ignore
            } finally {
                removeChatter(client, response);
                System.out.println("Now current number of user is " + clientRepository.size() + ".");
            }
        }
    }

}
