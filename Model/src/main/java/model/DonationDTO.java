package model;

public class DonationDTO implements Entity<Integer>{
    private Integer id;
    private Integer caseId;
    private Integer donorId;
    private double amountDonated;

    public DonationDTO(Integer id, Integer caseId, Integer donorId, double amountDonated) {
        this.id = id;
        this.caseId = caseId;
        this.donorId = donorId;
        this.amountDonated = amountDonated;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getDonorId() {
        return donorId;
    }

    public void setDonorId(Integer donorId) {
        this.donorId = donorId;
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
    public void setId(Integer integer) {
        this.id = integer;
    }
}
