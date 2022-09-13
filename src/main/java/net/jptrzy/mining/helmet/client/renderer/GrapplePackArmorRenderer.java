package net.jptrzy.mining.helmet.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;

public class GrapplePackArmorRenderer implements ArmorRenderer {
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {

        if(entity instanceof PlayerEntity player) {
            GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(player);
            if(gpc.isHooked()) {
//                BlockPos pos = gpc.getHookedBlockPos();
//                float length = pos.getY() - (float) entity.getPos().y + .5F;
//
//                matrices.push();
//
//
//                matrices.translate(0, 1.5F, .3F);
//                matrices.scale(2 / 1.9F, 2 / 1.9F, 2 / 1.9F);
                BlockPos block = gpc.getHookedBlockPos();

                Vec3f diff = new Vec3f(entity.getPos().add(
                        -block.getX()-0.5,
                        -entity.getPos().y,
                        -block.getZ()-0.5
                ));

                matrices.push();

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(true);

                matrices.scale(2 / 1.888f, 2 / 1.888f, 2 / 1.888f);
                matrices.translate(0, 1.414-1.4, 0);

                matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(entity.bodyYaw));

                Vec3f vec = new Vec3f(0, 0, 0.3f);
                vec.rotate(Vec3f.NEGATIVE_Y.getDegreesQuaternion(entity.bodyYaw));

                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
                MatrixStack.Entry entry = matrices.peek();
                Matrix4f matrix4f = entry.getPositionMatrix();



                float length = block.getY() - (float) entity.getPos().y - 1.4f;
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
    }
}
