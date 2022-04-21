package net.jamisonwolsborn.magmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.jamisonwolsborn.magmod.block.ModBlocks;
import net.minecraft.client.render.RenderLayer;

public class MagModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MAGNET, RenderLayer.getCutout());
    }
}
