package net.jptrzy.mining.helmet.integrations.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.client.model.MinerCharmModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MinerCharmTrinket implements Trinket, TrinketRenderer {
    private MinerCharmModel model;
    private static final Identifier MODEL_TEXTURE = Main.id("textures/armor/mining_charm.png");

    public static void register() {
        MinerCharmTrinket trinket = new MinerCharmTrinket();
        TrinketsApi.registerTrinket(Main.MINER_CHARM, trinket);
        TrinketRendererRegistry.registerRenderer(Main.MINER_CHARM, trinket);
    }

    public static int getLightLevel(LivingEntity entity){
        return TrinketsApi.getTrinketComponent(entity).get().isEquipped(Main.MINER_CHARM) ? 15 : 0;
    }

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        BipedEntityModel<LivingEntity> model = getModel();

        model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
        model.animateModel(entity, limbAngle, limbDistance, tickDelta);

        TrinketRenderer.followBodyRotations(entity, getModel());

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(MODEL_TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 255, 255, 255, 1);
    }

    public MinerCharmModel getModel() {
        if (model == null) {
            model = new MinerCharmModel();
        }
        return model;
    }
}
