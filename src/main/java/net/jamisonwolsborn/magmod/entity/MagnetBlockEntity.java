package net.jamisonwolsborn.magmod.entity;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class MagnetBlockEntity extends BlockEntity {
    public MagnetBlockEntity(BlockPos pos, BlockState state) {
        super(MagMod.MAGNET_BLOCK_ENTITY, pos, state);
    }
    // Serialize the BlockEntity
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

    }
}
