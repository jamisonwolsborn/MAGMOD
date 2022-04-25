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

public class MagnetBlockEntity extends BlockEntity {
    private double mag_dipole_scalar = 1.17*1/(1.257 * Math.pow(10,-6));
    public MagnetBlockEntity(BlockPos pos, BlockState state) {
        super(MagMod.MAGNET_BLOCK_ENTITY, pos, state);
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound tag) {
        // Save the current value of the number to the tag
        tag.putDouble("mag_dipole_scalar", mag_dipole_scalar);

        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        mag_dipole_scalar = tag.getDouble("mag_dipole_scalar");
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
