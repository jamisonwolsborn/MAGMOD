package net.jamisonwolsborn.magmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.jamisonwolsborn.magmod.block.ModBlocks;
import net.jamisonwolsborn.magmod.entity.MagnetBlockEntity;
import net.jamisonwolsborn.magmod.item.ModItems;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.jamisonwolsborn.magmod.block.ModBlocks.MAGNET;

public class MagMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("magmod");
	public static final String MOD_ID = "magmod";

	public static BlockEntityType<MagnetBlockEntity> MAGNET_BLOCK_ENTITY;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		MAGNET_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "tutorial:demo_block_entity", FabricBlockEntityTypeBuilder.create(MagnetBlockEntity::new, MAGNET).build(null));

		LOGGER.info("Hello Fabric world!");
	}
}
