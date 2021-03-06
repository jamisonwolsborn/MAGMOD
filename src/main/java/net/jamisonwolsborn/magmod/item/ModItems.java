package net.jamisonwolsborn.magmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jamisonwolsborn.magmod.MagMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    private  static final Item RUBY = registerItem( "ruby",
        new Item(new FabricItemSettings().group(ItemGroup.MISC)));
    private  static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MagMod.MOD_ID, name), item);
    }

    public  static  void registerModItems() {
        System.out.println("Registering Mod Items for " + MagMod.MOD_ID);
    }
}
