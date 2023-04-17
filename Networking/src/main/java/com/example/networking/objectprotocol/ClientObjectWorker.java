package com.example.networking.objectprotocol;

import com.example.services.IService;
import com.example.services.ITeledonObserver;
import com.example.services.ServiceException;
import model.Case;
import model.CaseDTO;
import model.Donation;
import model.Volunteer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class ClientObjectWorker implements Runnable, ITeledonObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleRequest(Request request){
        Response response=null;
        if (request instanceof LogInRequest){
            System.out.println("Login request ...");
            LogInRequest logReq=(LogInRequest)request;
            String username = logReq.getUsername();
            String password = logReq.getPassword();
            try {
                Optional<Volunteer> volunteerLoggedIn = server.volunteerLogIn(username, password, this);
                if (volunteerLoggedIn.isPresent()) {
                    return new VolunteerLogInResponse(volunteerLoggedIn.get());
                }
                else {
                    return new VolunteerLogInResponse(null);
                }
            } catch (ServiceException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        } else if (request instanceof GetAllCasesRequest) {
            System.out.println("Get all cases request ...");
            GetAllCasesRequest getAllCasesRequest = (GetAllCasesRequest)request;
            try {
                List<Case> cases = (List<Case>)server.getAllCases();
                return new GetAllCasesResponse(cases);
            } catch (ServiceException ex) {
                return new ErrorResponse( ex.getMessage());
            }
        } else if (request instanceof GetAllDonationsRequest) {
            System.out.println("Get all donations request ...");
            GetAllDonationsRequest getAllDonationsRequest = (GetAllDonationsRequest)request;
            try {
                List<Donation> donations =(List<Donation>) server.getAllDonations();
                return new GetAllDonationsResponse(donations);
            } catch(ServiceException ex) {
                return new ErrorResponse(ex.getMessage());
            }
        } else if (request instanceof MakeDonationRequest) {
            System.out.println("Make donation request ...");
            MakeDonationRequest makeDonationRequest = (MakeDonationRequest)request;
            try {
                CaseDTO selectedCase = makeDonationRequest.getSelectedCase();
                String donorName = makeDonationRequest.getDonorName();
                String donorAddress = makeDonationRequest.getDonorAddress();
                String donorPhone = makeDonationRequest.getDonorPhone();
                double amountDonated = makeDonationRequest.getAmountDonated();
                server.makeDonation(selectedCase,donorName,donorAddress,donorPhone,amountDonated);
                return new MakeDonationResponse();
            } catch(ServiceException ex) {
                return new ErrorResponse(ex.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    public void donationReceived() {
        try {
            sendResponse(new MakeDonationResponse());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
