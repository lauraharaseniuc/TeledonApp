package repository;

import model.Case;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.core.Logger;
import utils.JdbcUtils;


public class CaseRepositoryDB implements CaseRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = (Logger) LogManager.getLogger();

    public CaseRepositoryDB(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Case element) {
        logger.traceEntry("Saving the case {}", element);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into cases (description, city, country, suma, person) values (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1,element.getDescription());
            preparedStatement.setString(2,element.getCity());
            preparedStatement.setString(3,element.getCountry());
            preparedStatement.setString(4, element.getPersonInNeed());
            preparedStatement.setDouble(5, element.getNecessarySum());
            int result=preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("DB error: "+ e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Case element) {
        logger.traceEntry("Updating the case with the id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("update cases set description=?, city=?, country=?, suma=?, person=? where id=?")) {
            preparedStatement.setString(1,element.getDescription());
            preparedStatement.setString(2,element.getCity());
            preparedStatement.setString(3,element.getCountry());
            preparedStatement.setDouble(4,element.getNecessarySum());
            preparedStatement.setString(5,element.getPersonInNeed());
            preparedStatement.setInt(6, id);
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
        logger.traceEntry("Deleting the case with id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("delete from cases where id=?")) {
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
    public Iterable<Case> getAll() {
        logger.traceEntry("Getting all cases");
        Connection connection=dbUtils.getConnection();
        List<Case> caseList= new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from cases")) {
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id =resultSet.getInt("id");
                String description = resultSet.getString("description");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");
                Double necessarySum = resultSet.getDouble("suma");
                String person = resultSet.getString("person");
                Case donationCase = new Case(id, description, city, country, person, necessarySum);
                caseList.add(donationCase);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit(caseList);
        return caseList;
    }
}
