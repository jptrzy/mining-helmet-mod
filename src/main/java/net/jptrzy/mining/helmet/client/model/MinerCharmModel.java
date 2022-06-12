package net.jptrzy.mining.helmet.client.model;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class MinerCharmModel extends EntityModel<LivingEntity> {

    private final ModelData modelData;
    private final ModelPartData modelPartData;
    private final ModelPart root;

    public MinerCharmModel() {
        super();
        modelData = new ModelData();
        modelPartData = modelData.getRoot();
        modelPartData.addChild("base", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0, 0, 0, 3.0F, 4.0F, 3.0F),
                ModelTransform.pivot(-1.5F, -2F, -1.5F));
        root = modelPartData.createPart(16, 16);
    }

    @Override public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override public void animateModel(LivingEntity entity, float limbAngle, float limbDistance, float tickDelta) {}

    @Override public void setAngles(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if( MinecraftClient.isFancyGraphicsOrBetter() ){
            this.root.setAngles(
                MathHelper.cos(animationProgress * .1F ) * .1F,
                MathHelper.cos(animationProgress * .05F ) * .2F,
                0
            );

            this.root.setPivot(
                MathHelper.cos(animationProgress * .02F )*.5F,
                MathHelper.cos(animationProgress * .1F ),
                MathHelper.cos(animationProgress * .05F )*.5F
            );
        }
    }
}
