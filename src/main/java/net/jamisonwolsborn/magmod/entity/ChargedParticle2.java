package net.jamisonwolsborn.magmod.entity;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.world.World;

public class ChargedParticle2 extends EyeOfEnderEntity {

    public ChargedParticle2(EntityType<? extends ChargedParticle2> entityType, World world) {
        super(entityType, world);
    }

    public ChargedParticle2(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void tick() {

    }
}
