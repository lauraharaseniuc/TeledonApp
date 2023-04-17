package com.example.networking.objectprotocol;

import model.Donation;

import java.util.List;

public class GetAllDonationsResponse implements Response{
    private List<Donation> donationList;

    public GetAllDonationsResponse(List<Donation> donationList) {
        this.donationList = donationList;
    }

    public List<Donation> getDonationList() {
        return donationList;
    }
}
