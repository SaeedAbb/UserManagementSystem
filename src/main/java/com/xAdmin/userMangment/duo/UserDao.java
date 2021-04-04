package com.xAdmin.userMangment.duo;

import com.xAdmin.userMangment.bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is gonna mange the connection to the database and execute  the queries
 *
 * @author Saeed
 * @since 04.04.2021
 */

public class UserDao {

    private String jdbcURL = "jdbc:mysql://localhost:3306/userdb";
    private String jdbcUsername = "root";
    private String jdbcPassword = "abbasab-1";
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "
            + " (?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "SELECT id,name,email,country FROM users WHERE id=? ";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE ID=?;";
    private static final String UPDATE_USERS_SQL = "UPDATE users set name=?,email=?,country=?WHERE id=?;";

    /**
     * this method creates the connection the database
     *
     * @return the connection which has been created
     * @author Saeed
     * @since 04.04.2021
     */
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;

    }

    /**
     * this method inserts a new user to the database
     *
     * @param user the user which should be added to the database
     * @author Saeed
     * @since 04.04.2021
     */

    public void insertUser(User user) {
        System.out.println(INSERT_USERS_SQL);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    /**
     * this method selects a specific user from the database
     *
     * @param id the id of the user
     * @returnthe user which has been selected
     * @author Saeed
     * @since 04.04.2021
     */
    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    /**
     * this method selects all the users in the database and save them in an arrayList
     *
     * @return the list of the users in the database
     * @author Saeed
     * @since 04.04.2021
     */
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id, name, email, country));
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    /**
     * this method updates the user in the database
     *
     * @param user the user which will be edited
     * @return true if the user's information could be edited successfully
     * @author Saeed
     * @since 04.04.2021
     */

    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            System.out.println("updated user" + preparedStatement);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setInt(4, user.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            printSQLException(e);
        }

        return rowUpdated;
    }

    /**
     * this method deletes the user from the database
     *
     * @param id the id of the user
     * @return true if the user could be deleted false if not
     * @author Saeed
     * @since 04.04.2021
     */

    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            printSQLException(e);
        }
        return rowDeleted;

    }

    /**
     * this method is gonna handle all the exceptions
     *
     * @param exception the exception which will be thrown if something went wrong
     * @author Saeed
     * @since 04.04.2021
     */

    private void printSQLException(SQLException exception) {
        for (Throwable e : exception) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable throwable = e.getCause();
                while (throwable != null) {
                    System.out.println("Cause:" + throwable);
                    throwable = throwable.getCause();
                }
            }
        }
    }

}
