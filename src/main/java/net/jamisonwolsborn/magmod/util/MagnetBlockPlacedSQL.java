package net.jamisonwolsborn.magmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

import java.sql.*;
import java.text.SimpleDateFormat;

public class MagnetBlockPlacedSQL {
    public static String pos_ = null;
    public static String mag_moment_ = null;
    public MagnetBlockPlacedSQL(String pos, String mag_moment) {
        pos_ = pos;
        mag_moment_ = mag_moment;
    }

    public static void main() {
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Connection connection = null;
        Statement statement = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String sql = "INSERT INTO mag_positions VALUES ('" + pos_ + "', '" + mag_moment_ + "','" + sdf2.format(timestamp) + "')";
            statement.executeUpdate(sql);

            try (ResultSet rs = statement.executeQuery("SELECT * FROM mag_positions")) {
                while (rs.next()) {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    Text message = Text.of("Position: " + rs.getString("pos") + ", Magnetic Moment: " + rs.getString("m"));
                    mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
                }
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found

            MinecraftClient mc = MinecraftClient.getInstance();
            Text message = Text.of(e.getMessage());
            mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());

            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                MinecraftClient mc = MinecraftClient.getInstance();
                Text message = Text.of(e.getMessage());
                mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
            }
        }
    }
}
