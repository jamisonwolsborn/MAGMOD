package net.jamisonwolsborn.magmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ChargedParticleEntity extends ChargedParticleEntityTrajectory {
    protected ChargedParticleEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super((EntityType<? extends ThrownItemEntity>) entityType, world);
    }
    public ChargedParticleEntity(World world, LivingEntity owner) {
        super(null, owner, world); // null will be changed later
    }

    public ChargedParticleEntity(World world, double x, double y, double z) {
        super(null, x, y, z, world); // null will be changed later
    }

    @Override
    protected Item getDefaultItem() {
        return null; // We will configure this later, once we have created the ProjectileItem.
    }
}
