package net.jamisonwolsborn.magmod.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.cloth.api.client.events.v0.ClothClientHooks;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ProjectileTrajectory {
    static final boolean smoothLines = true;
    static double angleToHit = 0;

    public static void renderBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x1, y2, z1);

        GL11.glVertex3d(x2, y1, z1);
        GL11.glVertex3d(x2, y2, z1);

        GL11.glVertex3d(x1, y1, z2);
        GL11.glVertex3d(x1, y2, z2);

        GL11.glVertex3d(x2, y1, z2);
        GL11.glVertex3d(x2, y2, z2);
        //
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x2, y1, z1);

        GL11.glVertex3d(x1, y2, z1);
        GL11.glVertex3d(x2, y2, z1);

        GL11.glVertex3d(x1, y1, z2);
        GL11.glVertex3d(x2, y1, z2);

        GL11.glVertex3d(x1, y2, z2);
        GL11.glVertex3d(x2, y2, z2);
        //
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x1, y1, z2);

        GL11.glVertex3d(x2, y1, z1);
        GL11.glVertex3d(x2, y1, z2);

        GL11.glVertex3d(x1, y2, z1);
        GL11.glVertex3d(x1, y2, z2);

        GL11.glVertex3d(x2, y2, z1);
        GL11.glVertex3d(x2, y2, z2);;

    }

    public static void renderCurve(Camera camera, World world, BlockPos pos, float pitch, float yaw, double eye, PlayerEntity player, int[] color, float speed, float gravity, boolean[] booleans, int[] integers, boolean mainHand) {
        double d0 = camera.getPos().x;
        double d1 = camera.getPos().y - .005D;
        double d2 = camera.getPos().z;
        double accurateX = player.getX();
        double accurateY = player.getY();
        double accurateZ = player.getZ();

        RenderSystem.colorMask(true, false, false, false);
        float drag = 0.99f;
        float entityVelX = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float entityVelY = -MathHelper.sin(pitch * 0.017453292F);
        float entityVelZ = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        Vec3d vec3d = (new Vec3d(entityVelX, entityVelY, entityVelZ)).normalize().multiply(speed);
        Vec3d playerVelocity = player.getVelocity();
        vec3d = vec3d.add(playerVelocity.x, player.isOnGround() ? 0.0D : playerVelocity.y, playerVelocity.z);
        Vec3d entityVelocity = new Vec3d(vec3d.x, vec3d.y, vec3d.z);
        Vec3d entityPosition = new Vec3d(0, 0 + 1.5, 0);

        int playerside = 1;
        if (integers[0] != 2) {
            playerside = (integers[0] == 3 ? 1 : -1);
        } else {
            playerside = (mainHand ? 1 : -1);
        }

        double offsetX = 0 * Math.cos(Math.toRadians((yaw + 90) * -1)) + playerside * Math.sin(Math.toRadians((yaw + 90) * -1));
        double offsetZ = 0 * Math.sin(Math.toRadians((yaw + 90) * -1)) + playerside * Math.cos(Math.toRadians((yaw + 90) * -1));

        double prevX = 0;
        double prevZ = 0;

        SnowballEntity tempEntity = new SnowballEntity(world, player);

        for (int i=0; i < 100; i++) {
            HitResult hitResult = world.raycast(new RaycastContext(new Vec3d(accurateX + entityPosition.x, accurateY + entityPosition.y, accurateZ + entityPosition.z), new Vec3d(accurateX + entityPosition.x, accurateY + entityPosition.y, accurateZ + entityPosition.z).add(entityVelocity), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, tempEntity));
            if (hitResult.getType() != HitResult.Type.MISS) {
                double hitDistance = hitResult.getPos().distanceTo(player.getPos());
                double boxSize = hitDistance > 30 ? hitDistance/70 : 0.5;
                double defaultBoxSize = 0.5; // 0.5 = full block
                if (booleans[1]) { //Box visibility
                    renderBox(hitResult.getPos().x - defaultBoxSize - d0, hitResult.getPos().y - defaultBoxSize - d1, hitResult.getPos().z - defaultBoxSize - d2, hitResult.getPos().x + defaultBoxSize - d0, hitResult.getPos().y + defaultBoxSize - d1, hitResult.getPos().z + defaultBoxSize - d2);
                }
                if (booleans[2]) { //ApproxBox visibility
                    renderBox(hitResult.getPos().x - boxSize - d0, hitResult.getPos().y - boxSize - d1, hitResult.getPos().z - boxSize - d2, hitResult.getPos().x + boxSize - d0, hitResult.getPos().y + boxSize - d1, hitResult.getPos().z + boxSize - d2);
                }
                angleToHit = Math.acos(playerside / hitDistance); //finds the angle the line needs to point at to hit the target (since it's coming from the side, and changes depending on distance),
                // the "1" represents the opposite side, which we know. (opposite/adjacent).
                //
                // Good reference: https://www.khanacademy.org/math/geometry/hs-geo-trig/hs-geo-solve-for-an-angle/a/inverse-trig-functions-intro
                break;
            }

            if (booleans[0]) { //Line visibility
                GL11.glVertex3d(prevX + offsetX + (accurateX - d0), entityPosition.y + (accurateY - d1), prevZ + offsetZ + (accurateZ - d2));
            }

            entityPosition = entityPosition.add(entityVelocity);
            entityVelocity = entityVelocity.multiply(drag);
            entityVelocity = new Vec3d(entityVelocity.x, entityVelocity.y - gravity, entityVelocity.z);
            double newX = entityPosition.x * Math.cos(angleToHit-1.5708) - entityPosition.z * Math.sin(angleToHit-1.5708); // Rotation to point trajectory curve towards hit mark.
            double newZ = entityPosition.x * Math.sin(angleToHit-1.5708) + entityPosition.z * Math.cos(angleToHit-1.5708); //

            if (booleans[0]) { //Line visibility
                GL11.glVertex3d(newX + offsetX + (accurateX-d0), entityPosition.y + (accurateY - d1), newZ + offsetZ + (accurateZ - d2));
            }
            prevX = newX;
            prevZ = newZ;
        }
    }
    public static void main() {
        Set<Item> itemSimple = new HashSet<>();
        itemSimple.add(Items.SNOWBALL.asItem());

        Set<Item> itemComplex = new HashSet<>();
        itemComplex.add(Items.BOW.asItem());

        MinecraftClient client = MinecraftClient.getInstance();
        ClothClientHooks.DEBUG_RENDER_PRE.register(() -> {
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            if (smoothLines) GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(GL11.GL_LINES);

            PlayerEntity playerEntity = client.player;
            World world = client.world;
            BlockPos blockPos = new BlockPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ());
            Camera camera = client.gameRenderer.getCamera();
            float pitch = playerEntity.getPitch();
            float yaw = playerEntity.getYaw();
            double eye = playerEntity.getEyeY();
            ItemStack itemStack = playerEntity.getMainHandStack();
            ItemStack itemStackAlt = playerEntity.getOffHandStack();

            boolean mainHand = true;
            if (itemSimple.contains(itemStack.getItem()) || itemComplex.contains(itemStack.getItem())) { mainHand = true; }
            else if (itemSimple.contains(itemStackAlt.getItem()) || itemComplex.contains(itemStackAlt.getItem())) { mainHand = false; };

            if (itemSimple.contains(itemStack.getItem()) || itemSimple.contains(itemStackAlt.getItem())) {
                float speed = 1.5f;
                int[] color = {255, 0, 0};
                boolean[] booleans = {true, true, true};
                int[] integers = {2};
                ProjectileTrajectory.renderCurve(camera, world, blockPos, pitch, yaw, eye, playerEntity, color, speed, 0.03f, booleans, integers, mainHand);
            } else if (itemComplex.contains(itemStack.getItem()) ||  itemComplex.contains(itemStackAlt.getItem())) {
                float bowMultiplier = (72000.0f - playerEntity.getItemUseTimeLeft()) / 20.0f;
                bowMultiplier = (bowMultiplier * bowMultiplier + bowMultiplier * 2.0f) / 3.0f;
                if (bowMultiplier > 1.0f) {
                    bowMultiplier = 1.0f;
                }

                float speed = bowMultiplier * 3.0f;
                int[] color = {255, 0, 0};
                boolean[] booleans = {true, true, true};
                int[] integers = {2};
                ProjectileTrajectory.renderCurve(camera, world, blockPos, pitch, yaw, eye, playerEntity, color, speed, 0.05f, booleans, integers, mainHand);
            }


            GL11.glEnd();
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            if (smoothLines) GL11.glDisable(GL11.GL_LINE_SMOOTH);
        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
        });

    }
}
