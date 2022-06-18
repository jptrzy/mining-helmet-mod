package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.Debug;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method="updateInput", at=@At("HEAD"))
    void updateInput(float sidewaysSpeed, float forwardSpeed, boolean jumping, boolean sneaking, CallbackInfo ci){
        Debug.updateInput(sidewaysSpeed, forwardSpeed, jumping, sneaking, ci);
    }
}
