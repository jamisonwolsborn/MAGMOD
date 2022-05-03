package net.jamisonwolsborn.magmod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public interface MixinInterface {
    Vec3d crossProduct(Vec3d a, Vec3d b);
    void setVelocity(double x, double y, double z, float speed, float divergence);
    Vec3d getB(BlockPos pos);

    static float updateRotation(float prevRot, float newRot) {
        while (newRot - prevRot < -180.0f) {
            prevRot -= 360.0f;
        }
        while (newRot - prevRot >= 180.0f) {
            prevRot += 360.0f;
        }
        return MathHelper.lerp(0.2f, prevRot, newRot);
    }

    void updateRotation();
}
