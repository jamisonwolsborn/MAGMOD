package net.jamisonwolsborn.magmod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.sql.SQLException;

public class UpdateBlockBreakRunnable implements java.lang.Runnable {
    public Thread t;
    public World world_;
    public BlockPos pos_;
    private String threadName;
    boolean suspended = false;

    public UpdateBlockBreakRunnable(String name, World world, BlockPos pos) {
        world_ = world;
        pos_ = pos;
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);
        String pos_string = pos_.toShortString();
        MagnetBlockBreakSQL mb = new MagnetBlockBreakSQL(pos_string);
        mb.main();
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

