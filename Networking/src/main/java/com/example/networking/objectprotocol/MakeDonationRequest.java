package com.example.networking.objectprotocol;

import model.CaseDTO;

public class MakeDonationRequest implements Request{
    private CaseDTO selectedCase;
    private String donorName;
    private String donorAddress;
    private String donorPhone;
    private double amountDonated;

    public MakeDonationRequest(CaseDTO selectedCase, String donorName, String donorAddress, String donorPhone, double amountDonated) {
        this.selectedCase = selectedCase;
        this.donorName = donorName;
        this.donorAddress = donorAddress;
        this.donorPhone = donorPhone;
        this.amountDonated = amountDonated;
    }

    public CaseDTO getSelectedCase() {
        return selectedCase;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public double getAmountDonated() {
        return amountDonated;
    }
}
