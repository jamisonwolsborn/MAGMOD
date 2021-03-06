package net.jamisonwolsborn.magmod.util;

import net.jamisonwolsborn.magmod.MagMod;
import net.jamisonwolsborn.magmod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tech.tablesaw.api.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static net.jamisonwolsborn.magmod.block.MagnetBlock.FACING;

public class MagneticField {
    static final double mag_dipole_scalar = 930788;

    public static void main(String args[], World world, BlockPos pos) {
        Runnable R1 = new Runnable("Thread-1", world, pos);
        R1.start();

    }

    public static Direction getDirection(World world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Direction dir = bs.get(FACING);
        return dir;
    }

    public static void initializeSQL(World world, BlockPos pos) {
        Connection connection = null;
        Statement statement = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);

            update_block_placement(world, pos, statement);

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

    public static void update_block_placement(World world, BlockPos pos, Statement statement) throws SQLException {

        Direction dir = getDirection(world, pos);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (dir == Direction.EAST) {
            Table t = Table.read().file("extra_files/pos_x.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                int pos_x = (int) t.numberColumn("x").get(i);
                int pos_y = (int) t.numberColumn("y").get(i);
                int pos_z = (int) t.numberColumn("z").get(i);
                Number field_x_num = t.numberColumn("x_field").get(i);
                float field_x = field_x_num.floatValue();
                Number field_y_num = t.numberColumn("y_field").get(i);
                float field_y = field_y_num.floatValue();
                Number field_z_num = t.numberColumn("z_field").get(i);
                float field_z = field_z_num.floatValue();
                MagnetBlockPlacedSQL mp = new MagnetBlockPlacedSQL(dir, pos_x, pos_y, pos_z, field_x, field_y, field_z, statement);
                mp.main();
            }
        } else if (dir == Direction.WEST) {
            Table t = Table.read().file("extra_files/neg_x.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                int pos_x_num = (int) t.numberColumn("x").get(i);
                int pos_y_num = (int) t.numberColumn("y").get(i);
                int pos_z_num = (int) t.numberColumn("z").get(i);
                int pos_x = x + pos_x_num;
                int pos_y = y + pos_y_num;
                int pos_z = z + pos_z_num;

                Number field_x_num = t.numberColumn("x_field").get(i);
                float field_x = field_x_num.floatValue();
                Number field_y_num = t.numberColumn("y_field").get(i);
                float field_y = field_y_num.floatValue();
                Number field_z_num = t.numberColumn("z_field").get(i);
                float field_z = field_z_num.floatValue();
                MagnetBlockPlacedSQL mp = new MagnetBlockPlacedSQL(dir, pos_x, pos_y, pos_z, field_x, field_y, field_z, statement);
                mp.main();
            }
        } else if (dir == Direction.SOUTH) {
            Table t = Table.read().file("extra_files/pos_z.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                int pos_x_num = (int) t.numberColumn("x").get(i);
                int pos_y_num = (int) t.numberColumn("y").get(i);
                int pos_z_num = (int) t.numberColumn("z").get(i);
                int pos_x = x + pos_x_num;
                int pos_y = y + pos_y_num;
                int pos_z = z + pos_z_num;

                Number field_x_num = t.numberColumn("x_field").get(i);
                float field_x = field_x_num.floatValue();
                Number field_y_num = t.numberColumn("y_field").get(i);
                float field_y = field_y_num.floatValue();
                Number field_z_num = t.numberColumn("z_field").get(i);
                float field_z = field_z_num.floatValue();
                MagnetBlockPlacedSQL mp = new MagnetBlockPlacedSQL(dir, pos_x, pos_y, pos_z, field_x, field_y, field_z, statement);
                mp.main();
            }
        } else if (dir == Direction.NORTH) {
            Table t = Table.read().file("extra_files/neg_z.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                int pos_x_num = (int) t.numberColumn("x").get(i);
                int pos_y_num = (int) t.numberColumn("y").get(i);
                int pos_z_num = (int) t.numberColumn("z").get(i);
                int pos_x = x + pos_x_num;
                int pos_y = y + pos_y_num;
                int pos_z = z + pos_z_num;

                Number field_x_num = t.numberColumn("x_field").get(i);
                float field_x = field_x_num.floatValue();
                Number field_y_num = t.numberColumn("y_field").get(i);
                float field_y = field_y_num.floatValue();
                Number field_z_num = t.numberColumn("z_field").get(i);
                float field_z = field_z_num.floatValue();
                MagnetBlockPlacedSQL mp = new MagnetBlockPlacedSQL(dir, pos_x, pos_y, pos_z, field_x, field_y, field_z, statement);
                mp.main();
            }
        } else if (dir == Direction.UP) {
            Table t = Table.read().file("extra_files/pos_y.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                int pos_x_num = (int) t.numberColumn("x").get(i);
                int pos_y_num = (int) t.numberColumn("y").get(i);
                int pos_z_num = (int) t.numberColumn("z").get(i);
                int pos_x = x + pos_x_num;
                int pos_y = y + pos_y_num;
                int pos_z = z + pos_z_num;

                Number field_x_num = t.numberColumn("x_field").get(i);
                float field_x = field_x_num.floatValue();
                Number field_y_num = t.numberColumn("y_field").get(i);
                float field_y = field_y_num.floatValue();
                Number field_z_num = t.numberColumn("z_field").get(i);
                float field_z = field_z_num.floatValue();
                MagnetBlockPlacedSQL mp = new MagnetBlockPlacedSQL(dir, pos_x, pos_y, pos_z, field_x, field_y, field_z, statement);
                mp.main();
            }
        } else if (dir == Direction.DOWN) {
            Table t = Table.read().file("extra_files/neg_y.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                int pos_x_num = (int) t.numberColumn("x").get(i);
                int pos_y_num = (int) t.numberColumn("y").get(i);
                int pos_z_num = (int) t.numberColumn("z").get(i);
                int pos_x = x + pos_x_num;
                int pos_y = y + pos_y_num;
                int pos_z = z + pos_z_num;

                Number field_x_num = t.numberColumn("x_field").get(i);
                float field_x = field_x_num.floatValue();
                Number field_y_num = t.numberColumn("y_field").get(i);
                float field_y = field_y_num.floatValue();
                Number field_z_num = t.numberColumn("z_field").get(i);
                float field_z = field_z_num.floatValue();
                MagnetBlockPlacedSQL mp = new MagnetBlockPlacedSQL(dir, pos_x, pos_y, pos_z, field_x, field_y, field_z, statement);
                mp.main();
            }
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of("Hello");
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
    }

    public static Vec3d getMagField(World world, BlockPos pos) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        Vec3d pos_vec = new Vec3d(x, y, z);
        final double perm_free_space = 4 * 3.14 * Math.pow(10, -7);

        final double mag_dipole_scalar = 930788; //1.17 * 1 / (1.257 * Math.pow(10, -6));

        Vec3d mag_field_old = new Vec3d(0, 0, 0);
        Vec3d mag_field = new Vec3d(0, 0, 0);

        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                for (int k = -3; k <= 3; k++) {

                    double x_ = i + x;
                    double y_ = j + y;
                    double z_ = k + z;

                    Block block = (world.getBlockState(new BlockPos(x_, y_, z_))).getBlock();
                    BlockState bs = world.getBlockState(new BlockPos(x_, y_, z_));

                    if (i == 0 && j == 0 && k == 0) {
                        // check if pos(x,y,z) is magnet block
                        MinecraftClient mc = MinecraftClient.getInstance();
                        Text message = Text.of("Zero'd Out");
                        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
                    } else {
                        if (block == ModBlocks.MAGNET) {
                            Vec3d vec = new Vec3d(x_, y_, z_);
                            Vec3d vec_prime = new Vec3d(x_ - x, y_ - y, z_ - z);
                            double r = pos_vec.distanceTo(vec);
                            Vec3d r_hat = vec_prime.multiply(1 / r);

                            Direction dir = bs.get(FACING);
                            MinecraftClient mc = MinecraftClient.getInstance();

                            Vec3d mag_moment = new Vec3d(0, 0, 0);

                            if (dir == Direction.EAST) {
                                mag_moment = new Vec3d(mag_dipole_scalar, 0, 0);
                            } else if (dir == Direction.WEST) {
                                mag_moment = new Vec3d(-mag_dipole_scalar, 0, 0);
                            } else if (dir == Direction.SOUTH) {
                                mag_moment = new Vec3d(0, 0, mag_dipole_scalar);
                            } else if (dir == Direction.NORTH) {
                                mag_moment = new Vec3d(0, 0, -mag_dipole_scalar);
                            } else if (dir == Direction.UP) {
                                mag_moment = new Vec3d(0, mag_dipole_scalar, 0);
                            } else if (dir == Direction.DOWN) {
                                mag_moment = new Vec3d(0, -mag_dipole_scalar, 0);
                            } else {
                                mag_moment = new Vec3d(0 , 0, 0);
                            }

                            Text direction = Text.of(String.valueOf(dir) + " : Direction" + String.valueOf(mag_moment));
                            mc.inGameHud.addChatMessage(MessageType.SYSTEM, direction, mc.player.getUuid());

                            double mag_field_constants = perm_free_space / (4 * 3.14) * 3;
                            mag_field_old = (r_hat.multiply(mag_moment.dotProduct(r_hat)).subtract(mag_moment)).multiply(1 / Math.pow(r, 3)).multiply(mag_field_constants);
                            mag_field = mag_field.add(mag_field_old);

                            // MinecraftClient mc = MinecraftClient.getInstance();
                            Text message = Text.of(String.valueOf(mag_field) + " : End Loop");
                            mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
                        }
                    }
                }
            }
        }
        return mag_field;
    }
}
