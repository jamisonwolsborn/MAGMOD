package net.jamisonwolsborn.magmod.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class database {
    public static void main() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("drop table if exists mag_field");
            statement.executeUpdate("create table mag_field (pos string, mag_field string)");
            statement.executeUpdate("DROP TABLE IF EXISTS mag_positions");
            statement.executeUpdate("CREATE TABLE mag_positions (pos VARCHAR(255), m VARCHAR(255))");
            statement.executeUpdate("INSERT INTO mag_positions (pos, m) VALUES ('blah', 'Blah')");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}
