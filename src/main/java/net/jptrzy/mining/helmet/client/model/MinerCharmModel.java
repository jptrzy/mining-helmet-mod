package net.jptrzy.mining.helmet.client.model;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import static net.minecraft.client.render.entity.model.BipedEntityModel.getModelData;

public class MinerCharmModel extends EntityModel<LivingEntity> {

    private final ModelData modelData;
    private final ModelPartData modelPartData;
    private final ModelPart root;

    public MinerCharmModel() {
        super();
        modelData = new ModelData();
        modelPartData = modelData.getRoot();
        modelPartData.addChild("base", ModelPartBuilder.create()
//                        .mirrored()
                        .uv(0, 0)
                        .cuboid(0, 0, 0, 3.0F, 4.0F, 3.0F),
                ModelTransform.NONE);
        root = modelPartData.createPart(16, 16);
    }



//    public void test(BipedEntityModel<LivingEntity> contextModel){
//
//    }
//
//    private static ModelPart newParts() {
//
//        ModelData data = getModelData(Dilation.NONE, 0.0F);
//        ModelPartData child = data.getRoot().getChild("body");
//
//        child.addChild("base", ModelPartBuilder.create()
////                        .mirrored()
//                        .uv(0, 0)
//                        .cuboid(0, 0, 0, 3.0F, 4.0F, 3.0F),
//                ModelTransform.NONE);
//
//        //partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//        return child.createPart(16, 16);
//    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay);
    }

    @Override
    public void animateModel(LivingEntity entity, float limbAngle, float limbDistance, float tickDelta) {
    }

    @Override
    public void setAngles(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float x = MathHelper.cos(animationProgress * .02F )*.5F;
        float y = MathHelper.cos(animationProgress * .1F );
        float z = MathHelper.cos(animationProgress * .05F )*.5F;
        this.root.setPivot(x, y, z);
    }
}
