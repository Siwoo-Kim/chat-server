package com.siwoo.repository;

import com.siwoo.client.Client;

import java.util.List;

public interface ClientRepository {

    List<Client> getClients();
    Client save(Client client);
    long size();
}
