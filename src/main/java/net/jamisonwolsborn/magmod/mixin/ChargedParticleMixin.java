package net.jamisonwolsborn.magmod.mixin;


import net.jamisonwolsborn.magmod.MagMod;
import net.jamisonwolsborn.magmod.entity.ChargedParticleEntity;
import net.jamisonwolsborn.magmod.util.MixinInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.sql.*;

@Mixin (ThrownEntity.class)
public abstract class ChargedParticleMixin extends Entity implements MixinInterface {

    public ChargedParticleMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    public double mass  = 1;
    public double charge = 1;


    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        Vec3d vec3d = new Vec3d(x, y, z).normalize().add(this.random.nextGaussian() * (double)0.0075f * (double)divergence, this.random.nextGaussian() * (double)0.0075f * (double)divergence, this.random.nextGaussian() * (double)0.0075f * (double)divergence).multiply(speed);
        this.setVelocity(vec3d);
        double d = vec3d.horizontalLength();
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }
    @Override
    public Vec3d crossProduct(Vec3d a, Vec3d b) {
        return new Vec3d(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    @Override
    public Vec3d getB(BlockPos pos) {
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
        if (rs_float == null) {
            rs_float[0] = 0f;
            rs_float[1] = 0f;
            rs_float[2] = 0f;
        }
        Vec3d b_field = new Vec3d(rs_float[0], rs_float[1], rs_float[2]);
        return b_field;
    }

    @Override
    public void updateRotation() {
        Vec3d vec3d = this.getVelocity();
        double d = vec3d.horizontalLength();
        this.setPitch(MixinInterface.updateRotation(this.prevPitch, (float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875)));
        this.setYaw(MixinInterface.updateRotation(this.prevYaw, (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875)));
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;"), cancellable = true)
    private void injected(CallbackInfo ci) {
        if (this.getDisplayName().toString().contains("entity.magmod.charged_particle")) {
            float drag;
            Vec3d vec3d = this.getVelocity();
            MagMod.LOGGER.info("Starting Velocity = " + String.valueOf(vec3d));
            Vec3d bField = getB(this.getBlockPos());
            Vec3d force = crossProduct(vec3d, bField).multiply(charge);
            Vec3d acceleration = force.multiply(1 / mass);
            Vec3d mag_velocity = acceleration.multiply(1);
            vec3d = new Vec3d(mag_velocity.x + vec3d.x, mag_velocity.y + vec3d.y, mag_velocity.z + vec3d.z);
            double d = this.getX() + vec3d.x;
            double e = this.getY() + vec3d.y;
            double f = this.getZ() + vec3d.z;
            double horizontalLength = vec3d.horizontalLength();

            if (this.isTouchingWater()) {
                for (int i = 0; i < 4; ++i) {
                    float g = 0.25f;
                    this.world.addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25, e - vec3d.y * 0.25, f - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
                }
                drag = 0.8f;
            } else {
                drag = 0.99f;
            }
            this.setVelocity(vec3d.multiply(drag));
            if (!this.hasNoGravity()) {
                Vec3d vec3d2 = this.getVelocity();
                this.setVelocity(vec3d2.x, vec3d2.y - (double) .03f, vec3d2.z);
            }
            this.setPosition(d, e, f);

            ci.cancel();
        }
    }
}
