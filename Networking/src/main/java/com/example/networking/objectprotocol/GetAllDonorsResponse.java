package com.example.networking.objectprotocol;

import model.Donor;

import java.util.List;

public class GetAllDonorsResponse implements Response{
    private List<Donor> donorList;

    public GetAllDonorsResponse(List<Donor> donorList) {
        this.donorList = donorList;
    }

    public List<Donor> getDonorList() {
        return donorList;
    }
}
