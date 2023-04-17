package com.example.services;

import model.*;

import java.util.Optional;

public interface IService {
    Optional<Volunteer> volunteerLogIn(String username, String password, ITeledonObserver teledonObserver) throws ServiceException;
    Iterable<Case> getAllCases() throws ServiceException;
    Iterable<Donation> getAllDonations() throws ServiceException;
    Iterable<Donor> getAllDonors();
    void makeDonation(CaseDTO selectedCase, String donorName, String donorAddress, String donorPhone, double amountDonated) throws ServiceException;
    void volunteerLogOut(Volunteer volunteerLoggedIn, ITeledonObserver teledonObserver);
}
