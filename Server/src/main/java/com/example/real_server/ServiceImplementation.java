package com.example.real_server;

import com.example.services.IService;
import com.example.services.ITeledonObserver;
import com.example.services.ServiceException;
import model.*;
import repository.CaseRepository;
import repository.DonationRepository;
import repository.DonorRepository;
import repository.VolunteerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IService {
    private CaseRepository caseRepository;
    private VolunteerRepository volunteerRepository;
    private DonationRepository donationRepository;
    private DonorRepository donorRepository;
    private Map<Integer, ITeledonObserver> loggedVolunteers;

    public ServiceImplementation(CaseRepository caseRepository, VolunteerRepository volunteerRepository, DonationRepository donationRepository, DonorRepository donorRepository) {
        this.caseRepository = caseRepository;
        this.volunteerRepository = volunteerRepository;
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.loggedVolunteers = new ConcurrentHashMap<>();
    }

    public synchronized Optional<Volunteer> volunteerLogIn(String username, String password, ITeledonObserver teledonObserver) throws ServiceException {
        Optional<Volunteer> volunteerOptional = this.volunteerRepository.getVolunteerOnUsernameAndPassword(username, password);
        Volunteer volunteerLoggedIn = volunteerOptional.get();
        if (volunteerLoggedIn!=null){
            if(loggedVolunteers.get(volunteerLoggedIn.getId())!=null)
                throw new ServiceException("User already logged in.");
            loggedVolunteers.put(volunteerLoggedIn.getId(), teledonObserver);

        }
        else {
            throw new ServiceException("Authentication failed.");
        }
        return volunteerOptional;
    }

    public synchronized Iterable<Case> getAllCases(){
        return this.caseRepository.getAll();
    }
    public synchronized Iterable<Donation> getAllDonations() {
        return this.donationRepository.getAll();
    }

    public synchronized Iterable<Donor> getAllDonors() {
        return this.donorRepository.getAll();
    }

    private final int defaultThreadsNo=5;
    public synchronized void makeDonation(CaseDTO selectedCase, String donorName, String donorAddress, String donorPhone, double amountDonated) {
        Donor donor = this.donorRepository.addDonor(donorName, donorAddress, donorPhone);
        Integer caseId=selectedCase.getId();
        this.donationRepository.add(new Donation(new Case(caseId), donor, amountDonated));

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for (HashMap.Entry<Integer, ITeledonObserver> entry: this.loggedVolunteers.entrySet()) {
            ITeledonObserver observer = entry.getValue();
            executor.execute(() -> {
                observer.donationReceived(selectedCase, amountDonated);
            });
        }
        executor.shutdown();
    }

    public void volunteerLogOut (Volunteer volunteerLoggedIn, ITeledonObserver teledonObserver) {

    }
}
