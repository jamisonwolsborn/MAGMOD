package net.jamisonwolsborn.magmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MagnetBlockBreakSQL {
    public static String pos_string_ = null;

    public MagnetBlockBreakSQL(String pos_string) {
        pos_string_ = pos_string;
    }

    public static void main() {
        Connection connection = null;
        Statement statement = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String sql = "DELETE FROM mag_positions WHERE pos = '" + pos_string_ + "'";
            MinecraftClient mc = MinecraftClient.getInstance();
            Text message = Text.of("Break Position: " + pos_string_);
            mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
            statement.executeUpdate(sql);
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
                System.err.println(e.getMessage());
            }
        }
    }
}
