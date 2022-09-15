package net.jptrzy.mining.helmet.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.client.model.GrapplePackArmorModel;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;

public class GrapplePackArmorRenderer implements ArmorRenderer {
    private GrapplePackArmorModel model;
    public static final Identifier GRAPPLE_PACK_ARMOR_MODEL_TEXTURE = Main.id("textures/armor/grapple_pack.png");


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        VertexConsumer vertexConsumer;
        float length = 0;

        if(entity instanceof PlayerEntity player) {
            GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(player);
            if(gpc.isHooked()) {
                matrices.push();
                BlockPos block = gpc.getHookedBlockPos();

                Vec3f diff = new Vec3f(entity.getPos().add(
                        -block.getX()-0.5,
                        -entity.getPos().y,
                        -block.getZ()-0.5
                ));

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(true);

                matrices.scale(2 / 1.888f, 2 / 1.888f, 2 / 1.888f);
                matrices.translate(0, 1.414-1.4, 0);

                matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(entity.bodyYaw));

                Vec3f vec = new Vec3f(0, 0, 0.3f);
                vec.rotate(Vec3f.NEGATIVE_Y.getDegreesQuaternion(entity.bodyYaw));

                vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
                MatrixStack.Entry entry = matrices.peek();
                Matrix4f matrix4f = entry.getPositionMatrix();

                length = block.getY() - (float) entity.getPos().y - 1.4f;
                float vertX = .03F;
                float vertZ = 0;

                for (int i = 0; i < 2; i++) {
                    vertexConsumer.vertex(matrix4f, -vertX-diff.getX(), -length, -vertZ+diff.getZ()).color(0, 255, 255, 1).light(light).next();
                    vertexConsumer.vertex(matrix4f, vertX-diff.getX(), -length, vertZ+diff.getZ()).color(0, 255, 255, 1).light(light).next();
                    vertexConsumer.vertex(matrix4f, -vertX-vec.getX(), 0, -vertZ+vec.getZ()).color(0, 255, 255, 1).light(light).next();
                    vertexConsumer.vertex(matrix4f, vertX-vec.getX(), 0, vertZ+vec.getZ()).color(0, 255, 255, 1).light(light).next();
                    vertZ = vertX;
                    vertX = 0;
                }

                matrices.pop();
            }
        }

        GrapplePackArmorModel model = getModel();
        contextModel.setAttributes(model);
        model.setAngles(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        model.cogwheel.pitch = 0;
        model.cogwheel.yaw = 0;

        if (length != 0) {
            model.cogwheel.roll = length/2;
        } else if (model.cogwheel.roll > 0) {
            model.cogwheel.roll -= 0.1;
        }

        vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(GRAPPLE_PACK_ARMOR_MODEL_TEXTURE), false, stack.hasGlint());
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }

    public GrapplePackArmorModel getModel() {
        if (true || model == null) {
            model = new GrapplePackArmorModel();
        }
        return model;
    }
}
