package net.jptrzy.mining.helmet.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.client.renderer.MiningHelmetArmorRenderer;
import net.minecraft.client.render.RenderLayer;

public class Client implements ClientModInitializer {

    public static final MiningHelmetArmorRenderer MINING_HELMET_ARMOR_RENDERER = new MiningHelmetArmorRenderer();

    @Override
    public void onInitializeClient() {
        ArmorRenderer.register(MINING_HELMET_ARMOR_RENDERER, Main.MINING_HELMET);
        BlockRenderLayerMap.INSTANCE.putBlock(Main.ELDERIUM_ORE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Main.ELDERIUM_ORE_BLOCK, RenderLayer.getTranslucent());
    }
}
