package net.jptrzy.mining.helmet.mixin;

import com.mojang.authlib.GameProfile;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.jptrzy.mining.helmet.network.NetworkHandler;
import net.jptrzy.mining.helmet.network.message.TryHookingMessage;
import net.jptrzy.mining.helmet.network.message.UpdateInputMessage;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow public Input input;
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;



    private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(method = "tickMovement", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, at=@At(
        value="INVOKE", ordinal=2, shift= At.Shift.BY, by=-1,
        target="net/minecraft/client/network/ClientPlayerEntity.getAbilities ()Lnet/minecraft/entity/player/PlayerAbilities;"
    ))
    public void tickMovement(CallbackInfo ci, boolean bl, boolean bl4) {
        if(!bl && this.input.jumping && !this.getAbilities().flying && !this.isCreative()){
            if(this.abilityResyncCountdown == 0 && this.input.jumping && !bl4){
                this.abilityResyncCountdown = 7;
            }else{
                NetworkHandler.sendToServer(new TryHookingMessage());
            }
        }
    }

    @Inject(method="sendMovementPackets", at=@At("HEAD"))
    private void sendMovementPackets(CallbackInfo ci) {
        if(ModComponents.GRAPPLE_PACK.get(this).isHooked()){
            NetworkHandler.sendToServer(new UpdateInputMessage(this.jumping));
        }
    }

}
