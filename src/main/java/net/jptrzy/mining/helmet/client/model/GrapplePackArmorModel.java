package net.jptrzy.mining.helmet.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3f;

public class GrapplePackArmorModel extends BipedEntityModel<LivingEntity> {

    public final ModelPart cogwheel;

    public GrapplePackArmorModel() {
        super(newParts());

        cogwheel = this.body.getChild("cogwheel");
        cogwheel.scale(new Vec3f(1f,1f,1f));

        this.body.visible = true;
        this.rightArm.visible = false;
        this.leftArm.visible = false;
        this.head.visible = false;
        this.hat.visible = false;
        this.rightLeg.visible = false;
        this.leftLeg.visible = false;
    }

    private static ModelPart newParts() {
        ModelData data = getModelData(Dilation.NONE, 10.0F);
        ModelPartData modelPartData = data.getRoot().getChild("body");
        // Start GrapplePackModel

        ModelPartData cogwheel = modelPartData.addChild("cogwheel", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -2.0F, -0.55F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.0F, -0.5F, -0.55F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -1.0F, -0.3F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 6.0F, 6.3F));

        ModelPartData cube_r1 = cogwheel.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -0.05F, 0.0F, 0.0F, -0.7854F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -24.0F, 2.0F, 5.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.5F, -23.0F, 2.0F, 7.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        // End GrapplePackModel
        return data.getRoot().createPart(64, 64);
    }
}
