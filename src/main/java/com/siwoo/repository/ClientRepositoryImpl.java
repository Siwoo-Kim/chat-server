package com.siwoo.repository;

import com.siwoo.client.Client;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ClientRepositoryImpl implements ClientRepository {
    static private List<Client> database;

    static {
        database = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public List<Client> getClients() {
        return new ArrayList<>(database);
    }

    @Override
    public Client save(Client client) {
        if(!database.contains(client)) {
            database.add(client);
        }
        return client;
    }

    @Override
    public long size() {
        return database.size();
    }

    @Test
    public void test() {
        ClientRepository repository = new ClientRepositoryImpl();
        Client client = new Client.Builder()
                .name("test1")
                .build();
        repository.save(client);
        client = new Client.Builder()
                .name("test2")
                .build();
        repository.save(client);

        assertEquals(repository.getClients().size(),2);
        System.out.println(repository.getClients());
    }
}
