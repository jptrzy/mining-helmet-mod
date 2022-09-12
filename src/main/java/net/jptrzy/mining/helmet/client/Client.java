package net.jptrzy.mining.helmet.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.client.renderer.GrapplePackArmorRenderer;
import net.jptrzy.mining.helmet.client.renderer.MiningHelmetArmorRenderer;
import net.jptrzy.mining.helmet.init.ModItems;
import net.minecraft.client.render.RenderLayer;

public class Client implements ClientModInitializer {

    public static final MiningHelmetArmorRenderer MINING_HELMET_ARMOR_RENDERER = new MiningHelmetArmorRenderer();
    public static final GrapplePackArmorRenderer GRAPPLE_PACK_ARMOR_RENDERER = new GrapplePackArmorRenderer();

    @Override
    public void onInitializeClient() {
        ArmorRenderer.register(MINING_HELMET_ARMOR_RENDERER, ModItems.MINING_HELMET);
        ArmorRenderer.register(GRAPPLE_PACK_ARMOR_RENDERER, ModItems.GRAPPLE_PACK);

        BlockRenderLayerMap.INSTANCE.putBlock(Main.ELDERIUM_ORE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Main.ELDERIUM_ORE_BLOCK, RenderLayer.getTranslucent());
    }
}
