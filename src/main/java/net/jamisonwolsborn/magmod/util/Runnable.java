package net.jamisonwolsborn.magmod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.jamisonwolsborn.magmod.util.MagneticField.getMagField;
public class Runnable implements java.lang.Runnable {
    public Thread t;
    public World world_;
    public BlockPos pos_;
    private String threadName;
    boolean suspended = false;

    public Vec3d mag_field;

    public Runnable(String name, World world, BlockPos pos) {
        world_ = world;
        pos_ = pos;
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);

        mag_field = getMagField(world_, pos_);

        System.out.println("Thread " + threadName + " exiting.");
    }

    public void start() {
        System.out.println("Starting " + threadName);

        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    void suspend() {
        suspended = true;
    }

    synchronized void resume() {
        suspended = false;
        notify();
    }
}
