package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.integrations.trinkets.MinerCharmTrinket;
import net.jptrzy.mining.helmet.util.PlayerProperties;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerProperties {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }


    // Setup PlayerProperties for GrapplePack
    @Inject(method="readCustomDataFromNbt", at=@At("TAIL")) public void readNbt(NbtCompound tag, CallbackInfo info) {
        dataTracker.set(Main.DataTrackers.HOOKED_TRACKER, tag.getBoolean("hasHook"));
        dataTracker.set(Main.DataTrackers.BLOCK_TRACKER, NbtHelper.toBlockPos(tag.getCompound("hookedBlockPos")));
    }
    @Inject(method="readCustomDataFromNbt", at=@At("TAIL")) public void writeNbt(NbtCompound tag, CallbackInfo info) {
        tag.putBoolean("hooked", dataTracker.get(Main.DataTrackers.HOOKED_TRACKER));
        tag.put("hookedBlockPos",  NbtHelper.fromBlockPos(dataTracker.get(Main.DataTrackers.BLOCK_TRACKER)));

    }
    @Inject(method="initDataTracker", at=@At("HEAD")) public void initTracker(CallbackInfo info) {
        dataTracker.startTracking(Main.DataTrackers.HOOKED_TRACKER, false);
        dataTracker.startTracking(Main.DataTrackers.BLOCK_TRACKER, new BlockPos(0, 0, 0));
    }

    @Override public boolean isHooked() {
        return dataTracker.get(Main.DataTrackers.HOOKED_TRACKER);
    }
    @Override public void setHooked(boolean hasHook) {
        dataTracker.set(Main.DataTrackers.HOOKED_TRACKER, hasHook);
    }

    @Override public BlockPos getHookedBlock() {
        return dataTracker.get(Main.DataTrackers.BLOCK_TRACKER);
    }
    @Override public void setHookedBlock(BlockPos pos) {
        dataTracker.set(Main.DataTrackers.BLOCK_TRACKER, pos);
    }


    // Block parrots from sitting on the player shoulder when ghost in a bottle equipped.
    @Redirect(method="addShoulderEntity", at=@At(
            value="INVOKE", target = "net/minecraft/nbt/NbtCompound.isEmpty ()Z", ordinal = 1
    ))
    public boolean isRightShoulderEmpty(NbtCompound instance) {
        return !(Main.TRINKETS_LOADED && MinerCharmTrinket.isEquipped((PlayerEntity) (Object) this)) && instance.isEmpty();
    }

    @Inject(method="tickMovement", at=@At("HEAD"), cancellable = true) public void tickMovement(CallbackInfo ci) {
//        if (((PlayerProperties) this).isHooked()){
//            ci.cancel();
//        }
    }

    @Override public float getMovementSpeed(){
         return ((PlayerProperties) this).isHooked() ? 0 : super.getMovementSpeed();
    }

//    @Override public void move(MovementType movementType, Vec3d movement) {
//        if(!((PlayerProperties) this).isHooked()){
//            super.move(movementType, movement);
//        }
//    }

    @Shadow public void increaseTravelMotionStats(double dx, double dy, double dz){};

    @Inject(method="travel", at=@At("HEAD"), cancellable = true) public void travel(CallbackInfo ci) {
        if(((PlayerProperties) this).isHooked()){
            this.move(MovementType.SELF, this.getVelocity());
            this.increaseTravelMotionStats(0, 0, 0);
            this.updateLimbs(this, this instanceof Flutterer);
            this.fallDistance = 0;
            ci.cancel();
        }
    }

//    @Inject(method="increaseTravelMotionStats", at=@At("HEAD"), cancellable = true) public void increaseTravelMotionStats(CallbackInfo ci) {
//        if(((PlayerProperties) this).isHooked()){
//            ci.cancel();
//        }
//    }

//    @Redirect(method="updatePose", at=@At(
//            value="INVOKE", target = "net/minecraft/entity/player/PlayerEntity.isSneaking ()Z"
//    ))
//    public boolean _isSneaking(PlayerEntity player) {
//        Main.LOGGER.warn(!((PlayerProperties) player).isHooked() && player.isSneaking());
//        return !((PlayerProperties) player).isHooked() && player.isSneaking();
//    }

//    @Inject(method="updatePose", at=@At("TAIL"), cancellable = true) public void updatePose(CallbackInfo ci) {
//        if (((PlayerProperties) this).isHooked()) {
//            this.setPose(EntityPose.STANDING);
//        }
//    }
}
