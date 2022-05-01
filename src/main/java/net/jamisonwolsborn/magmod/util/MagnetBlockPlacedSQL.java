package net.jamisonwolsborn.magmod.util;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.util.math.Direction;

import java.sql.*;
import java.text.SimpleDateFormat;

public class MagnetBlockPlacedSQL {
    public static int pos_x_;
    public static int pos_y_;
    public static int pos_z_;
    public static float field_x_;
    public static float field_y_;
    public static float field_z_;
    public static Direction dir_ = Direction.NORTH;
    public static Statement statement_;

    public MagnetBlockPlacedSQL(Direction dir, int pos_x, int pos_y, int pos_z, float field_x, float field_y, float field_z, Statement statement) {
        pos_x_ = pos_x;
        pos_y_ = pos_y;
        pos_z_ = pos_z;
        field_x_ = field_x;
        field_y_ = field_y;
        field_z_ = field_z;
        dir_ = dir;
        statement_ = statement;
    }

    public static String createUpdateString(float field_x, float field_y, float field_z) {
        String sql = "UPDATE mag SET field_x = " + field_x + ", field_y = " + field_y + ", field_z = " + field_z + ", Timestamp = '" + createTimestamp() + "' WHERE pos_x = " + pos_x_ + " AND pos_y = " + pos_y_ + " AND pos_z = " + pos_z_ + ";";

        return sql;
    }

    public static float[] readMagValues(int pos_x, int pos_y, int pos_z, Statement statement) {
        float[] mag_fields = {};
        float[] rs_float = null;
        String select_string = "SELECT field_x, field_y, field_z FROM mag WHERE pos_x = '" + pos_x + "' AND pos_y = '" + pos_y + "' AND pos_z = '" + pos_z + "';";
        try (ResultSet rs = statement.executeQuery(select_string)) {
            while (rs.next()) {
                rs_float = new float[]{rs.getFloat("field_x"), rs.getFloat("field_y"), rs.getFloat("field_z")};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs_float;
    }

    public static float[] getFieldSum(float[] mag_fields) {
        float[] updatedFieldSum = {field_x_ + mag_fields[0], field_y_ + mag_fields[1], field_z_ + mag_fields[2]};
        return updatedFieldSum;
    }

    public static String createTimestamp() {
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf2.format(timestamp);
    }

    public static String createInsertString() {
        String sql = "INSERT INTO mag VALUES ('" + pos_x_ + "', '" + pos_y_ + "','" + pos_z_ + "', '" + field_x_ + "','" + field_y_ + "','" + field_z_ + "','" + createTimestamp() + "');";
        return sql;
    }

    public static void main() {
        float[] mag_values = readMagValues(pos_x_, pos_y_, pos_z_, statement_);
        String sql;

        if (mag_values == null) {
            sql = createInsertString();
        } else {
            float[] updateValues = getFieldSum(mag_values);
            sql = createUpdateString(updateValues[0], updateValues[1], updateValues[2]);
        }
        try {
            statement_.executeUpdate(sql);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            MagMod.LOGGER.info(e.getMessage());
        }
    }
}
