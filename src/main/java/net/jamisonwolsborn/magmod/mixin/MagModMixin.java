package net.jamisonwolsborn.magmod.mixin;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@Mixin(TitleScreen.class)
public class MagModMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS 'mag' (id INTEGER PRIMARY KEY AUTOINCREMENT, pos_x INT, pos_y INT, pos_z INT, field_x FLOAT, field_y FLOAT, field_z FLOAT, Timestamp DATETIME, block_id VARCHAR[255])");
        } catch (SQLException e) {
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
        MagMod.LOGGER.info("This line is printed by an example mod mixin!");
    }
}
