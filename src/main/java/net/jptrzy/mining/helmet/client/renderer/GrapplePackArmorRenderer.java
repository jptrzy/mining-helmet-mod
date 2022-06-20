package net.jptrzy.mining.helmet.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.util.PlayerProperties;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
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
            PlayerProperties prop = (PlayerProperties) player;
            if(true || prop.isHooked()) {


                matrices.push();
                matrices.translate(0, 1, 0);
                matrices.scale(1/1.9F, 1/1.9F,1/1.9F);

                BlockPos pos = prop.getHookedBlock().down();
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());

                MatrixStack.Entry entry = matrices.peek();
                Matrix4f matrix4f = entry.getPositionMatrix();
                Matrix3f matrix3f = entry.getNormalMatrix();


                int q = entity.world.getLightLevel(LightType.BLOCK, pos);

                float vertX = .2F;
                float vertY = 5;
                float vertZ = 0;

                for(int i=0; i<2;i++) {
                    vertexConsumer.vertex(matrix4f, -vertX, -vertY, -vertZ).color(0, 255, 0, 255).light(q).next();
                    vertexConsumer.vertex(matrix4f, vertX, -vertY, vertZ).color(0, 255, 0, 255).light(q).next();
                    vertexConsumer.vertex(matrix4f, -vertX, vertY, -vertZ).color(0, 255, 0, 255).light(q).next();
                    vertexConsumer.vertex(matrix4f, vertX, vertY, vertZ).color(0, 255, 0, 255).light(q).next();
                    vertZ = vertX;
                    vertX = 0;
                }


                matrices.pop();
            }
        }
    }
}
