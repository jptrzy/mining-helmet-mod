package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract boolean isInsideWall();

    @Inject(method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", at = @At("RETURN"))
    public void travel(Vec3d velocity, CallbackInfo ci) {
        if((Entity) (Object) this instanceof PlayerEntity) {
            Main.LOGGER.warn("T0 {}", velocity.y);
        }
    }

    @Inject(method = "setVelocity(DDD)V", at = @At("RETURN"))
    public void _travel(double x, double y, double z, CallbackInfo ci) {
        if((Entity) (Object) this instanceof PlayerEntity) {
            Main.LOGGER.warn("T1 {}", y);
        }
    }


}
