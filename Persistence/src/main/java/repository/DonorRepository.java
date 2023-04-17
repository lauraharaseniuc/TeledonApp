package repository;

import model.Donor;

public interface DonorRepository extends GenericRepository<Integer, Donor>{
    Donor addDonor(String donorName, String donorAddress, String donorPhone);
}
