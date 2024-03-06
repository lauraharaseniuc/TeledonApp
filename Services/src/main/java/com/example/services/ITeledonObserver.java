package com.example.services;

import model.CaseDTO;

public interface ITeledonObserver {
    void donationReceived(CaseDTO selectedCase, double amountDonated);
}
