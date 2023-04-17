package repository;

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

public class DonorRepositoryDB implements DonorRepository{
    private JdbcUtils dbUtils;
    private static final Logger logger = (Logger) LogManager.getLogger();

    public DonorRepositoryDB(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Donor element) {
        logger.traceEntry("Saving the donor {}", element);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into donors (name, address, phone) values (?, ?, ?)")) {
            preparedStatement.setString(1,element.getName());
            preparedStatement.setString(2,element.getAddress());
            preparedStatement.setString(3,element.getPhoneNumber());
            int result=preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("DB error: "+ e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Donor element) {
        logger.traceEntry("Updating the donor with the id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("update donors set name=?, address=?, phone=? where id=?")) {
            preparedStatement.setString(1,element.getName());
            preparedStatement.setString(2,element.getAddress());
            preparedStatement.setString(3,element.getPhoneNumber());
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
        logger.traceEntry("Deleting the donor with id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("delete from donors where id=?")) {
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
    public Iterable<Donor> getAll() {
        logger.traceEntry("Getting all cases");
        Connection connection=dbUtils.getConnection();
        List<Donor> donorList= new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from donors")) {
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id =resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone");
                Donor donor = new Donor(id, name, address, phoneNumber);
                donorList.add(donor);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit(donorList);
        return donorList;
    }

    @Override
    public Donor addDonor(String donorName, String donorAddress, String donorPhone) {
        logger.traceEntry("adding a donor if it does not already exist");
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from donors where name=? and address=? and phone=?")) {
            preparedStatement.setString(1, donorName);
            preparedStatement.setString(2, donorAddress);
            preparedStatement.setString(3, donorPhone);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id =resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone");
                Donor donor = new Donor(id, name, address, phoneNumber);
                return donor;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit("Exiting addDonor");
        this.add(new Donor(donorName, donorAddress, donorPhone));
        return this.addDonor(donorName, donorAddress, donorPhone);
    }
}
