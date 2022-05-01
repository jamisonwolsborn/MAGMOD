package net.jamisonwolsborn.magmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ChargedParticleEntityTrajectory extends ThrownEntity implements FlyingItemEntity {
    public ChargedParticleEntityTrajectory(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ChargedParticleEntityTrajectory(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public ChargedParticleEntityTrajectory(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public ItemStack getStack() {
        return null;
    }

    /*@Override
    public void tick() {
        super.tick();

    }*/

    protected abstract Item getDefaultItem();
}
