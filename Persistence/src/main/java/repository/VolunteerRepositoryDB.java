package repository;

import model.Volunteer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class VolunteerRepositoryDB implements VolunteerRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = (Logger) LogManager.getLogger();

    public VolunteerRepositoryDB(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }
    @Override
    public void add(Volunteer element) {
        logger.traceEntry("Saving the volunteer {}", element);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("insert into volunteers (username, name, password) values (?, ?, ?)")) {
            preparedStatement.setString(1,element.getUsername());
            preparedStatement.setString(2,element.getName());
            preparedStatement.setString(3,element.getPassword());
            int result=preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("DB error: "+ e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Volunteer element) {
        logger.traceEntry("Updating the volunteer with the id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("update volunteers set username=?, name=?, password=? where id=?")) {
            preparedStatement.setString(1,element.getUsername());
            preparedStatement.setString(2,element.getName());
            preparedStatement.setString(3,element.getPassword());
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
        logger.traceEntry("Deleting the volunteer with id {}", id);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("delete from volunteers where id=?")) {
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
    public Iterable<Volunteer> getAll() {
        logger.traceEntry("Getting all volunteers");
        Connection connection=dbUtils.getConnection();
        List<Volunteer> volunteerList= new ArrayList<>();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from volunteers")) {
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id =resultSet.getInt("id");
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                Volunteer volunteer = new Volunteer(id, username, name, password);
                volunteerList.add(volunteer);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit(volunteerList);
        return volunteerList;
    }

    @Override
    public Optional<Volunteer> getVolunteerOnUsernameAndPassword(String username, String password) {
        logger.traceEntry("Getting volunteer on username {0} and password", username);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preparedStatement= connection.prepareStatement("select * from volunteers where username=? and password=?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id =resultSet.getInt("id");
                String volunteerUsername = resultSet.getString("username");
                String volunteerName = resultSet.getString("name");
                String volunteerPassword = resultSet.getString("password");
                Volunteer volunteer = new Volunteer(id, volunteerUsername, volunteerName, volunteerPassword);
                return Optional.of(volunteer);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println(e.getMessage());
        }
        logger.traceExit();
        return Optional.empty();
    }
}
