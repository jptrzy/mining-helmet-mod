package net.jptrzy.mining.helmet.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jptrzy.mining.helmet.Main;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class RopeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public RopeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        matrices.push();

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();

        float length = 10;
        float vertX = .03F;
        float vertZ = 0;
        for (int i = 0; i < 2; i++) {
            vertexConsumer.vertex(matrix4f, -vertX, -1.3F, -vertZ).color(0, 255, 0, 1).light(light).next();
            vertexConsumer.vertex(matrix4f, vertX, -1.3F, vertZ).color(0, 255, 0, 1).light(light).next();
            vertexConsumer.vertex(matrix4f, -vertX, -length, -vertZ-.3F).color(0, 255, 0, 1).light(light).next();
            vertexConsumer.vertex(matrix4f, vertX, -length, vertZ-.3F).color(0, 255, 0, 1).light(light).next();
            vertZ = vertX;
            vertX = 0;
        }

        matrices.pop();

    }
}
