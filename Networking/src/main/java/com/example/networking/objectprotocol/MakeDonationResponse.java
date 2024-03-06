package com.example.networking.objectprotocol;

import model.CaseDTO;

public class MakeDonationResponse implements UpdateResponse{
    private CaseDTO selectedCase;
    private double amountDonated;

    public MakeDonationResponse(CaseDTO selectedCase, double amountDonated) {
        this.selectedCase = selectedCase;
        this.amountDonated = amountDonated;
    }

    public CaseDTO getSelectedCase() {
        return selectedCase;
    }

    public double getAmountDonated() {
        return amountDonated;
    }
}
