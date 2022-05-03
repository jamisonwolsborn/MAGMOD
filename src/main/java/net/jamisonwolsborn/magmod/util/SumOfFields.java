package net.jamisonwolsborn.magmod.util;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.util.math.BlockPos;

import javax.swing.text.Position;
import java.sql.*;

public class SumOfFields {

    public static float[] sumUp(BlockPos pos) {
        Connection connection = null;
        Statement statement = null;

        int pos_x = pos.getX();
        int pos_y = pos.getY();
        int pos_z = pos.getZ();

        float[] rs_float = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String sql = "SELECT SUM(field_x) AS x_sum, SUM(field_y) AS y_sum, SUM(field_z) AS z_sum FROM mag WHERE pos_x = '" + pos_x + "' AND pos_y = '" + pos_y + "' AND pos_z = '" + pos_z + "';";

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    rs_float = new float[]{rs.getFloat("x_sum"), rs.getFloat("y_sum"), rs.getFloat("z_sum")};
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (
                SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            MagMod.LOGGER.info(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                MagMod.LOGGER.info(e.getMessage());
            }
        }
        return rs_float;
    }
}
