package com.example.networking.objectprotocol;

import com.example.services.IService;
import com.example.services.ITeledonObserver;
import com.example.services.ServiceException;
import model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesObjectProxy implements IService {
    private String host;
    private int port;

    private ITeledonObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses=new LinkedBlockingQueue<Response>();
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleUpdate(UpdateResponse update){
        if (update instanceof MakeDonationResponse) {
            MakeDonationResponse makeDonationResponse = (MakeDonationResponse)update;
            CaseDTO selectedCase = makeDonationResponse.getSelectedCase();
            double amountDonated = makeDonationResponse.getAmountDonated();
            this.client.donationReceived(selectedCase, amountDonated);
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                        handleUpdate((UpdateResponse)response);
                    }else{
                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request)throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }
    private Response readResponse() {
        Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Optional<Volunteer> volunteerLogIn(String username, String password, ITeledonObserver teledonObserver) throws ServiceException{
        initializeConnection();
        sendRequest(new LogInRequest(username, password));
        Response response=readResponse();
        if (response instanceof VolunteerLogInResponse){
            this.client=teledonObserver;
            return Optional.of (((VolunteerLogInResponse) response).getVolunteerLoggedIn());
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ServiceException(err.getMessage());
        }
        return Optional.empty();
    }

    public Iterable<Case> getAllCases() throws ServiceException{
        this.sendRequest(new GetAllCasesRequest());
        Response response = this.readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.getMessage());
        }
        GetAllCasesResponse casesResponse = (GetAllCasesResponse) response;
        List<Case> caseList = casesResponse.getCases();
        return caseList;
    }

    public Iterable<Donation> getAllDonations() throws ServiceException{
        this.sendRequest(new GetAllDonationsRequest());
        Response response = this.readResponse();
        if (response instanceof  ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.getMessage());
        }
        GetAllDonationsResponse donationsResponse = (GetAllDonationsResponse)response;
        List<Donation> donationList = donationsResponse.getDonationList();
        return donationList;
    }

    public Iterable<Donor> getAllDonors() throws ServiceException{
        this.sendRequest(new GetAllDonorsRequest());
        Response response = this.readResponse();
        if (response instanceof  ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.getMessage());
        }
        GetAllDonorsResponse donorsResponse = (GetAllDonorsResponse) response;
        List<Donor> donorList = donorsResponse.getDonorList();
        return donorList;
    }
    public void makeDonation(CaseDTO selectedCase, String donorName, String donorAddress, String donorPhone, double amountDonated) throws ServiceException{
        this.sendRequest(new MakeDonationRequest(selectedCase, donorName, donorAddress, donorPhone, amountDonated));
        Response response = this.readResponse();
        if (response instanceof  ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.getMessage());
        }
    }

    public void volunteerLogOut(Volunteer volunteerLoggedIn, ITeledonObserver teledonObserver) {

    }
}

