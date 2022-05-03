package net.jamisonwolsborn.magmod.util;

import com.mojang.datafixers.types.templates.Sum;
import net.jamisonwolsborn.magmod.MagMod;
import net.jamisonwolsborn.magmod.entity.MagnetBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static net.jamisonwolsborn.magmod.block.MagnetBlock.FACING;

public class batchCommitSQL {

    public static void sendChat(String chat) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of("Your Message Is: " + chat);
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
    }
    public static String createTimestamp() {
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf2.format(timestamp);
    }

    public static void initializeSQL(World world, BlockPos pos) {
        Connection connection;
        BlockState bs = world.getBlockState(pos);
        Direction dir = bs.get(FACING);
        String csvFilePath = null;

        /*MagnetBlockEntity mag_block = new MagnetBlockEntity(pos, bs);
        NbtCompound mag_block_nbt = mag_block.createNbtWithId();
        String block_id = String.valueOf(mag_block_nbt.getUuid("block_id"));*/
        String block_id = pos.toShortString();
        sendChat(block_id);

        if (dir == Direction.EAST) {
            csvFilePath = "extra_files/pos_x.csv";
        }
        else if (dir == Direction.WEST) {
            csvFilePath = "extra_files/neg_x.csv";
        }
        else if (dir == Direction.SOUTH) {
            csvFilePath = "extra_files/pos_z.csv";
        }
        else if (dir == Direction.NORTH) {
            csvFilePath = "extra_files/neg_z.csv";
        }
        else if (dir == Direction.UP) {
            csvFilePath = "extra_files/pos_y.csv";
        }
        else if (dir == Direction.DOWN) {
            csvFilePath = "extra_files/neg_y.csv";
        }

        int batchSize = 100;

        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);

            String sql = "INSERT INTO mag (pos_x, pos_y, pos_z, field_x, field_y, field_z, Timestamp, block_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;

            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String pos_x = data[1];
                String pos_y = data[2];
                String pos_z = data[3];
                String field_x = data[4];
                String field_y = data[5];
                String field_z = data[6];

                int iPos_x = Integer.parseInt(pos_x) + pos.getX();
                statement.setInt(1, iPos_x);
                int iPos_y = Integer.parseInt(pos_y) + pos.getY();
                statement.setInt(2, iPos_y);
                int iPos_z = Integer.parseInt(pos_z) + pos.getZ();
                statement.setInt(3, iPos_z);

                float fField_x = Float.parseFloat(field_x);
                statement.setFloat(4, fField_x);
                float fField_y = Float.parseFloat(field_y);
                statement.setFloat(5, fField_y);
                float fField_z = Float.parseFloat(field_z);
                statement.setFloat(6, fField_z);

                String timestamp = createTimestamp();
                statement.setString(7, timestamp);

                statement.setString(8, block_id);

                statement.addBatch();

                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            connection.commit();
            connection.close();

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            MagMod.LOGGER.info(e.getMessage());
        } catch (FileNotFoundException e) {
            MagMod.LOGGER.info(e.getMessage());
        } catch (IOException e) {
            MagMod.LOGGER.info(e.getMessage());
        }

        // float[] field_float = SumOfFields.sumUp(pos);
        // String field_float_x = String.valueOf(field_float[0]);
        // String field_float_y = String.valueOf(field_float[1]);
        // String field_float_z = String.valueOf(field_float[2]);

        // sendChat(field_float_x + ", " + field_float_y + ", " + field_float_z);

    }

}
