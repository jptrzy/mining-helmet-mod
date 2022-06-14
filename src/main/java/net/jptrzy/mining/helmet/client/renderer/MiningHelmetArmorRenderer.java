package net.jptrzy.mining.helmet.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.mining.helmet.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class MiningHelmetArmorRenderer implements ArmorRenderer {
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        String modelId = Main.MOD_ID + ":mining_helmet#inventory";

        matrices.push();
        ((BipedEntityModel<? extends LivingEntity>) contextModel).head.rotate(matrices);

        matrices.translate(0D, -.5D, 0D);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        // Minimal scale for helmet not clipping with outer layer of the skin (with 3d skin layer mod).
        matrices.scale(1.05F, 1.05F, 1.05F);

        itemRenderer.renderItem(stack, ModelTransformation.Mode.NONE, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, itemRenderer.getModels().getModelManager().getModel(new ModelIdentifier(modelId)));
        matrices.pop();
    }
}
