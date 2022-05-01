package net.jamisonwolsborn.magmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.sql.SQLException;

import static net.jamisonwolsborn.magmod.util.MagneticField.initializeSQL;
import static net.jamisonwolsborn.magmod.util.MagneticField.update_block_placement;

public class UpdateBlockPosRunnable implements java.lang.Runnable {
    public Thread t;
    public World world_;
    public BlockPos pos_;
    private String threadName;
    boolean suspended = false;

    public UpdateBlockPosRunnable(String name, World world, BlockPos pos) {
        world_ = world;
        pos_ = pos;
        threadName = name;
        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of("Creating " + threadName);
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);
        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of("Running " + threadName);
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());

        initializeSQL(world_, pos_);

        Text message_ = Text.of("Thread " + threadName + " exiting.");
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message_, mc.player.getUuid());
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

