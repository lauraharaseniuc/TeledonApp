package com.example.real_server;

import com.example.services.IService;
import com.example.networking.utils.AbstractServer;
import com.example.networking.utils.ServerException;
import com.example.networking.utils.ObjectConcurrentServer;
import repository.*;

import java.io.IOException;
import java.util.Properties;

public class StartObjectServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/teledonserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find teledonserver.properties "+e);
            return;
        }
        DonationRepository donationRepository = new DonationRepositoryDB(serverProps);
        DonorRepository donorRepository = new DonorRepositoryDB(serverProps);
        VolunteerRepository volunteerRepository = new VolunteerRepositoryDB(serverProps);
        CaseRepository caseRepository = new CaseRepositoryDB(serverProps);
        IService serviceImplementation = new ServiceImplementation(caseRepository, volunteerRepository, donationRepository, donorRepository);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, serviceImplementation);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
