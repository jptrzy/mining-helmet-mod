package net.jptrzy.mining.helmet.integrations.trinkets;

import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.client.model.MinerCharmModel;
import net.jptrzy.mining.helmet.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class GrapplePackTrinket extends OptionalTrinket implements Trinket {
    private MinerCharmModel model;
    private static final Identifier MODEL_TEXTURE = Main.id("textures/models/ghost.png");

    public static void register() {
        GrapplePackTrinket trinket = new GrapplePackTrinket();
        TrinketsApi.registerTrinket(ModItems.GRAPPLE_PACK, trinket);
//        TrinketRendererRegistry.registerRenderer(Main.MINER_CHARM, trinket);
    }

    public static int getLightLevel(LivingEntity entity){
        return TrinketsApi.getTrinketComponent(entity).get().isEquipped(ModItems.GRAPPLE_PACK) ? 15 : 0;
    }

    public static boolean isEquipped(LivingEntity entity){
        return TrinketsApi.getTrinketComponent(entity).get().isEquipped(ModItems.GRAPPLE_PACK);
    }

    /*
    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        EntityModel<LivingEntity> model = getModel();

        model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
        model.animateModel(entity, limbAngle, limbDistance, tickDelta);
        matrices.translate(-.55F, -.4F,-.1F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(MODEL_TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, .7F);

        RenderSystem.disableBlend();
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity instanceof PlayerEntity playerEntity) {
            playerEntity.dropShoulderEntity(playerEntity.getShoulderEntityRight());
            playerEntity.setShoulderEntityRight(new NbtCompound());
        }
    }

    public MinerCharmModel getModel() {
        if(model == null || Main.DEBUG) {
            model = new MinerCharmModel();
        }
        return model;
    }
    */
}
