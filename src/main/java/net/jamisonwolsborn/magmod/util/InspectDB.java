package net.jamisonwolsborn.magmod.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InspectDB {
    static final String DB_URL = "jdbc:sqlite:database.db";
    static final String QUERY = "SELECT * FROM mag_positions";

    public static void main(String[] args) {
        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
        ) {
            while(rs.next()){
                //Display values
                System.out.print("Position: " + rs.getString("pos"));
                System.out.println(", Magnetic Moment: " + rs.getString("m"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
