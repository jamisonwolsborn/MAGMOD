package net.jamisonwolsborn.magmod.entity;

import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.sql.*;


public abstract class ChargedParticleEntityTrajectory extends ThrownEntity implements FlyingItemEntity {

    public double charge = 1;
    public double mass = 1;

    public static void sendChat(String chat) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of("Your Message Is: " + chat);
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
    }

    public ChargedParticleEntityTrajectory(EntityType<? extends ChargedParticleEntityTrajectory> entityType, World world) {
        super(entityType, world);
    }

    public ChargedParticleEntityTrajectory(EntityType<ChargedParticleEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public ChargedParticleEntityTrajectory(EntityType<ChargedParticleEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public ItemStack getStack() {
        return null;
    }

    public Vec3d crossProduct(Vec3d a, Vec3d b) {
        return new Vec3d(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }
    public static Vec3d getB(BlockPos pos) {
        Connection connection = null;
        int pos_x = pos.getX();
        int pos_y = pos.getY();
        int pos_z = pos.getZ();

        float[] rs_float = new float[0];
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String sql = "SELECT SUM(field_x) AS x_sum, SUM(field_y) AS y_sum, SUM(field_z) AS z_sum FROM mag WHERE pos_x = '" + pos_x + "' AND pos_y = '" + pos_y + "' AND pos_z = '" + pos_z + "';";

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    rs_float = new float[]{rs.getFloat("x_sum"), rs.getFloat("y_sum"), rs.getFloat("z_sum")};
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

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

        Vec3d b_field = new Vec3d(rs_float[0], rs_float[1], rs_float[2]);
        return b_field;
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        boolean bl = false;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
            BlockState blockState = this.world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                this.setInNetherPortal(blockPos);
                bl = true;
            } else if (blockState.isOf(Blocks.END_GATEWAY)) {
                BlockEntity blockEntity = this.world.getBlockEntity(blockPos);
                if (blockEntity instanceof EndGatewayBlockEntity && EndGatewayBlockEntity.canTeleport(this)) {
                    EndGatewayBlockEntity.tryTeleportingEntity(this.world, blockPos, blockState, this, (EndGatewayBlockEntity)blockEntity);
                }

                bl = true;
            }
        }

        if (hitResult.getType() != HitResult.Type.MISS && !bl) {
            this.onCollision(hitResult);
        }

        this.checkBlockCollision();



        Vec3d vec3d = this.getVelocity();
        Vec3d bField = getB(this.getBlockPos());
        Vec3d force = crossProduct(vec3d, bField).multiply(charge);
        Vec3d acceleration = force.multiply(1/mass);
        Vec3d mag_velocity = acceleration.multiply(.02);

        vec3d = new Vec3d(mag_velocity.x + vec3d.x, mag_velocity.y + vec3d.y, mag_velocity.z + vec3d.z);

        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;


        this.updateRotation();
        float h;
        if (this.isTouchingWater()) {
            for(int i = 0; i < 4; ++i) {
                float g = 0.25F;
                this.world.addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25, e - vec3d.y * 0.25, f - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
            }

            h = 0.8F;
        } else {
            h = 0.99F;
        }
        sendChat(vec3d.toString());
        this.setVelocity(vec3d.multiply((double)h));
        if (!this.hasNoGravity()) {
            Vec3d vec3d2 = this.getVelocity();
            this.setVelocity(vec3d2.x, vec3d2.y - (double)this.getGravity(), vec3d2.z);
        }

        this.setPosition(d, e, f);
    }

    protected abstract Item getDefaultItem();
}
