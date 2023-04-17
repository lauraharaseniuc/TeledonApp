package com.example.networking.utils;

import com.example.services.IService;
import com.example.networking.objectprotocol.ClientObjectWorker;

import java.net.Socket;

public class ObjectConcurrentServer extends AbstractConcurrentServer {
    private IService server;
    public ObjectConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(this.server, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
