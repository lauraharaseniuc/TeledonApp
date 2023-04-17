package repository;

import model.Volunteer;

import java.util.Optional;

public interface VolunteerRepository extends GenericRepository<Integer, Volunteer>{
    Optional<Volunteer> getVolunteerOnUsernameAndPassword(String username, String password);
}
