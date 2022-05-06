package net.jamisonwolsborn.magmod.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.jamisonwolsborn.magmod.block.MagnetBlock.FACING;

public class MagnetBlockPlaced {
    public static Direction getDirection(BlockState bs) {
        Direction dir = bs.get(FACING);
        return dir;
    }

    public static Direction.Axis getAxis(BlockState bs) {
        Direction.Axis axis = getDirection(bs).getAxis();
        return axis;
    }

    public static void orientBlock(World world, BlockPos pos, BlockState bs) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        Direction dir = getDirection(bs);
        Direction.Axis axis = getAxis(bs);

        for (int i = -1; i <= 1; i++) {

            if (i != 0) {
                Block block = (world.getBlockState(pos)).getBlock();
                Block block_ = (world.getBlockState(new BlockPos(x + i, y, z))).getBlock();
                BlockState block_bs = world.getBlockState(new BlockPos(x + i, y, z));
                if (block == block_) {
                    if (dir != getDirection(block_bs).getOpposite() && dir != getDirection(block_bs) && axis != getAxis(block_bs)) {
                        world.setBlockState(new BlockPos(x + i, y, z), bs);
                    } else if (dir == getDirection(block_bs).getOpposite() && axis == Direction.Axis.X && getAxis(block_bs) == Direction.Axis.X) {
                        world.removeBlock(new BlockPos(x + i, y, z), false);
                        world.setBlockState(new BlockPos(x + 2 * i, y, z), block_bs);
                    }
                } else if (block != block_) {
                    break;
                }
            }
        }
        for (int j = -1; j <= 1; j++) {
            if (j != 0) {
                Block block = (world.getBlockState(pos)).getBlock();
                Block block_ = (world.getBlockState(new BlockPos(x, y + j, z))).getBlock();
                BlockState block_bs = world.getBlockState(new BlockPos(x, y + j, z));
                if (block == block_) {
                    if (dir != getDirection(block_bs).getOpposite() && dir != getDirection(block_bs) && axis != getAxis(block_bs)) {
                        world.setBlockState(new BlockPos(x, y + j, z), bs);
                    } else if (dir == getDirection(block_bs).getOpposite() && axis == Direction.Axis.Y && getAxis(block_bs) == Direction.Axis.Y) {
                        world.removeBlock(new BlockPos(x, y + j, z), false);
                        world.setBlockState(new BlockPos(x, y + 2*j, z), block_bs);
                    }
                } else if (block != block_) {
                    break;
                }
            }
        }
        for (int k = -1; k <= 1; k++) {
            if (k != 0) {
                Block block = (world.getBlockState(pos)).getBlock();
                Block block_ = (world.getBlockState(new BlockPos(x, y, z + k))).getBlock();
                BlockState block_bs = world.getBlockState(new BlockPos(x, y, z + k));
                if (block == block_) {
                    if (dir != getDirection(block_bs).getOpposite() && dir != getDirection(block_bs) && axis != getAxis(block_bs)) {
                        world.setBlockState(new BlockPos(x, y, z + k), bs);
                    } else if (dir == getDirection(block_bs).getOpposite() && axis == Direction.Axis.Z && getAxis(block_bs) == Direction.Axis.Z) {
                        world.removeBlock(new BlockPos(x, y, z + k), false);
                        world.setBlockState(new BlockPos(x, y, z + k*2), block_bs);
                    }
                } else if (block != block_) {
                    break;
                }
            }
        }
    }
}
