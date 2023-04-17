package model;

import java.io.Serializable;

public class Donation implements Entity<Integer>, Serializable {
    private Integer id;
    private Case donationCase;
    private Donor donor;
    private double amountDonated;

    public Donation(Case donationCase, Donor donor, double amountDonated) {
        this.donationCase = donationCase;
        this.donor = donor;
        this.amountDonated = amountDonated;
    }

    public Donation(Integer id, Case donationCase, Donor donor, double amountDonated) {
        this.id = id;
        this.donationCase = donationCase;
        this.donor = donor;
        this.amountDonated = amountDonated;
    }

    public Case getDonationCase() {
        return donationCase;
    }

    public void setDonationCase(Case donationCase) {
        this.donationCase = donationCase;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public double getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(double amountDonated) {
        this.amountDonated = amountDonated;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "id=" + id +
                ", caseId=" + donationCase.getId() +
                ", donorId=" + donor.getId() +
                ", amountDonated=" + amountDonated +
                '}';
    }
}
