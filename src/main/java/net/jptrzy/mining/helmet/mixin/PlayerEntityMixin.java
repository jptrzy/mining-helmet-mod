package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.jptrzy.mining.helmet.integrations.trinkets.MinerCharmTrinket;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // SHADOWS
    @Shadow public void increaseTravelMotionStats(double dx, double dy, double dz){};

    // Block parrots from sitting on the player shoulder when ghost in a bottle equipped.
    @Redirect(method="addShoulderEntity", at=@At(
            value="INVOKE", target = "net/minecraft/nbt/NbtCompound.isEmpty ()Z", ordinal = 1
    ))
    public boolean isRightShoulderEmpty(NbtCompound instance) {
        return !(Main.TRINKETS_LOADED && MinerCharmTrinket.isEquipped((PlayerEntity) (Object) this))
                && instance.isEmpty();
    }

    @Override public float getMovementSpeed(){
         return ModComponents.GRAPPLE_PACK.get(this).isHooked() ? 0 : super.getMovementSpeed();
    }

    /*
    Remove possibility for player movement while in air.
    Rest of it just remove fall damage while landing when hooked and ensure that everything works ok (like anim...).
    */
    @Inject(method="travel", at=@At("HEAD"), cancellable = true) public void travel(Vec3d movementInput, CallbackInfo ci) {
        GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(this);
        if(gpc.isHooked()){
            ModComponents.GRAPPLE_PACK.get(this).isHooked();

            movementInput.multiply(1, 0, 1);

            Vec3d vec = movementInputToVelocity(movementInput, .07f, this.getYaw());

            BlockPos block = gpc.getHookedBlockPos();
//            Vec3d diff = this.getPos().add(-block.getX()-0.5, -this.getPos().y, -block.getZ()-0.5);
            Vec3d diff = this.getPos().add(
                vec.getX() -block.getX()-0.5,
                vec.getY() -this.getPos().y,
                vec.getZ() -block.getZ()-0.5
            );

            if (diff.length() > 0.1) {
                this.setVelocity(this.getVelocity().add(diff.multiply(-0.05)));
            }


            if (diff.length() < 2.5) {
                this.setVelocity(this.getVelocity().add(vec));
            }

//            Main.LOGGER.warn("Diff {}", diff.length());

//            new Vec3d(gpc.getHookedBlockPos());




            //            Entity.movementInputToVelocity(movementInput, 1, this.getYaw());


            this.move(MovementType.SELF, this.getVelocity());
            this.updateLimbs(this, this instanceof Flutterer);
            this.fallDistance = 0;
            this.increaseTravelMotionStats(0, 0, 0); // Update difference
            ci.cancel();
        }
    }

    @Inject(method="getBlockBreakingSpeed", at=@At("RETURN"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (!this.onGround && ModComponents.GRAPPLE_PACK.get(this).isHooked()) {
            cir.setReturnValue(cir.getReturnValue() * 5.0F);
        }
    }

    // UnHook Player on death
    @Inject(method="onDeath", at=@At("RETURN"), cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ModComponents.GRAPPLE_PACK.get(this).setHooked(false);
    }
}