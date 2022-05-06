package net.jamisonwolsborn.magmod.entity;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.datafixer.fix.ChunkPalettedStorageFix;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.MessageType;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

import static net.jamisonwolsborn.magmod.block.MagnetBlock.FACING;

public class MagnetBlockEntity extends BlockEntity {

    public static double moment = 1200;

    public MagnetBlockEntity(BlockPos pos, BlockState state) {
        super(MagMod.MAGNET_BLOCK_ENTITY, pos, state);
    }
    // Serialize the BlockEntity

    public static void sendChat(String chat) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of("Your Message Is: " + chat);
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        UUID uuid = UUID.randomUUID();

        tag.putUuid("block_id", uuid);

        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        UUID block_id = tag.getUuid("block_id");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    public static void tick(World world, BlockPos pos, BlockState state, MagnetBlockEntity be) {
        Block magBlock = (world.getBlockState(pos)).getBlock();
        Direction dir = state.get(FACING);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if ((dir == Direction.EAST) || (dir == Direction.WEST)){
            for (int i = -2; i <= 2; i++) {
                if (i != 0) {
                    BlockPos blockPos = new BlockPos(x + i, y, z);
                    Block block = (world.getBlockState(blockPos)).getBlock();
                    if (block == magBlock) {
                        BlockState bs = world.getBlockState(blockPos);
                        Direction dir_ = bs.get(FACING);
                        if ((dir_ == dir) || (dir_ == dir.getOpposite())) {
                            double force = 6 * Math.pow(10, -7) * Math.pow(moment, 2) / Math.pow(i, 4);
                            if (dir_ == dir) {
                                world.breakBlock(blockPos, false);
                                world.setBlockState(new BlockPos(x + Math.round(i / 2), y, z), bs);
                            } else if (dir_ == dir.getOpposite()) {
                                world.breakBlock(blockPos, false);
                                world.setBlockState(new BlockPos(x + 2 * i, y, z), bs);
                            }
                        }
                    }
                }
            }
        }
        else if ((dir == Direction.SOUTH) || (dir == Direction.NORTH)){
            for (int j = -2; j <= 2; j++) {
                if (j != 0) {
                    BlockPos blockPos = new BlockPos(x, y, z + j);
                    Block block = (world.getBlockState(blockPos)).getBlock();
                    if (block == magBlock) {
                        BlockState bs = world.getBlockState(blockPos);
                        Direction dir_ = bs.get(FACING);
                        if ((dir_ == dir) || (dir_ == dir.getOpposite())) {
                            double force = 6 * Math.pow(10, -7) * Math.pow(moment, 2) / Math.pow(j, 4);
                            if (dir_ == dir) {
                                world.breakBlock(blockPos, false);
                                world.setBlockState(new BlockPos(x, y + Math.round(j / 2), z), bs);
                            } else if (dir_ == dir.getOpposite()) {
                                world.breakBlock(blockPos, false);
                                world.setBlockState(new BlockPos(x, y + 2 * j, z), bs);
                            }
                        }
                    }
                }
            }
        }
        else if ((dir == Direction.UP) || (dir == Direction.DOWN))  {
            for (int k = -2; k <= 2; k++) {
                if (k != 0) {
                    BlockPos blockPos = new BlockPos(x, y, z + k);
                    Block block = (world.getBlockState(blockPos)).getBlock();
                    if (block == magBlock) {
                        BlockState bs = world.getBlockState(blockPos);
                        Direction dir_ = bs.get(FACING);
                        if ((dir_ == dir) || (dir_ == dir.getOpposite())) {
                            double force = 6 * Math.pow(10, -7) * Math.pow(moment, 2) / Math.pow(k, 4);
                            if (dir_ == dir) {
                                world.breakBlock(blockPos, false);
                                world.setBlockState(new BlockPos(x, y, z + Math.round(k / 2)), bs);
                            } else if (dir_ == dir.getOpposite()) {
                                world.breakBlock(blockPos, false);
                                world.setBlockState(new BlockPos(x, y, z + 2 * k), bs);
                            }
                        }
                    }
                }
            }
        }
    }
}
