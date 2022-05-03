package net.jamisonwolsborn.magmod.util;

import net.jamisonwolsborn.magmod.MagMod;
import net.jamisonwolsborn.magmod.entity.MagnetBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.swing.text.Position;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MagnetBlockBreakSQL {
    public static BlockPos pos_;
    public static World world_;

    public MagnetBlockBreakSQL(World world, BlockPos pos) {
        world_ = world;
        pos_ = pos;
    }

    public static void main() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String block_id = pos_.toShortString();

            statement.executeUpdate("DELETE FROM mag WHERE block_id = '" + block_id + "';");

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
    }
}

