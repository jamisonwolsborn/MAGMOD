package net.jamisonwolsborn.magmod.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.jamisonwolsborn.magmod.MagMod;

public class ModBlocks {
    public static final Block MAGNET = registerBlock("magnet",
            new Block(FabricBlockSettings.of(Material.STONE).strength(4.0f))); // add custom tool to break with tags here

    public static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registry.BLOCK, new Identifier(MagMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registry.ITEM, new Identifier(MagMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(ItemGroup.MISC)));
    }

    public static void registerModBlocks() {
        System.out.println("Registering ModBlocks for " + MagMod.MOD_ID);
    }
}
