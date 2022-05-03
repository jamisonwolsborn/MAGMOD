package net.jamisonwolsborn.magmod;

import jakarta.persistence.metamodel.IdentifiableType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.jamisonwolsborn.magmod.block.ModBlocks;
import net.jamisonwolsborn.magmod.entity.ChargedParticleEntity;
import net.jamisonwolsborn.magmod.entity.ChargedParticleEntityTrajectory;
import net.jamisonwolsborn.magmod.entity.MagnetBlockEntity;
import net.jamisonwolsborn.magmod.item.ChargedParticleItem;
import net.jamisonwolsborn.magmod.item.ModItems;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
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

	public static final Item ChargedParticleItem = new ChargedParticleItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));

	public static final EntityType<ChargedParticleEntity> ChargedParticleEntityType = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(MOD_ID, "charged_particle"),
			FabricEntityTypeBuilder.<ChargedParticleEntity>create(SpawnGroup.MISC, ChargedParticleEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
					.trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
					.build() // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
	);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "charged_particle"), ChargedParticleItem);
		
		MAGNET_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "magmod:magnet_block_entity", FabricBlockEntityTypeBuilder.create(MagnetBlockEntity::new, MAGNET).build(null));
		
		LOGGER.info("Hello Fabric world!");
	}
}
