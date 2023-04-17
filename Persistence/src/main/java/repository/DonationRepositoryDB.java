package repository;

import model.Case;
import model.Donation;
import model.Donor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonationRepositoryDB implements DonationRepository{
    private JdbcUtils dbUtils;
    private static final Logger logger = (Logger) LogManager.getLogger();

    public DonationRepositoryDB(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }
    @Override
    public void add(Donation element) {
        logger.traceEntry("Saving the donation {}", element);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into donations (case_id, donor_id, amount) values (?, ?, ?)")) {
            preparedStatement.setInt(1,element.getDonationCase().getId());
            preparedStatement.setInt(2,element.getDonor().getId());
            preparedStatement.setDouble(3,element.getAmountDonated());
            int result=preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("DB error: "+ e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Donation element) {
        logger.traceEntry("Updating the donation with the id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("update donations set case_id=?, donor_id=?, amount=? where id=?")) {
            preparedStatement.setInt(1,element.getDonationCase().getId());
            preparedStatement.setInt(2,element.getDonor().getId());
            preparedStatement.setDouble(3,element.getAmountDonated());
            preparedStatement.setInt(4, id);
            int result=preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting the donation with id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("delete from donations where id=?")) {
            preparedStatement.setInt(1,id);
            int result=preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Donation> getAll() {
        logger.traceEntry("Getting all donations");
        Connection connection=dbUtils.getConnection();
        List<Donation> donationList= new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from donations")) {
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id =resultSet.getInt("id");
                int caseId =resultSet.getInt("case_id");
                int donorId =resultSet.getInt("donor_id");
                double amountDonated = resultSet.getDouble("amount");
                Donation donation = new Donation(id, new Case(caseId), new Donor(donorId), amountDonated);
                donationList.add(donation);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit(donationList);
        return donationList;
    }
}
