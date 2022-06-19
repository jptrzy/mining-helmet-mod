package net.jptrzy.mining.helmet.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.jptrzy.mining.helmet.Debug;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.network.NetworkHandler;
import net.jptrzy.mining.helmet.network.message.TryHookingMessage;
import net.jptrzy.mining.helmet.network.message.UpdateInputMessage;
import net.jptrzy.mining.helmet.util.PlayerProperties;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
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
//    @Inject(method = "tickMovement", at=@At("RETURN"))
//    public void tickMovement(CallbackInfo ci) {
//        Debug.tickMovement(ci, (ClientPlayerEntity) (Object) this);
//    }

    // net/minecraft/client/network/ClientPlayerEntity.getAbilities ()Lnet/minecraft/entity/player/PlayerAbilities

    @Shadow public Input input;
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(method = "tickMovement", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, at=@At(
        value="INVOKE", ordinal=2, shift= At.Shift.BY, by=-1,
        target="net/minecraft/client/network/ClientPlayerEntity.getAbilities ()Lnet/minecraft/entity/player/PlayerAbilities;"
    ))
    public void tickMovement(CallbackInfo ci, boolean bl) {
        if(!bl && this.input.jumping && !this.getAbilities().flying){
            if(this.abilityResyncCountdown == 0){
                this.abilityResyncCountdown = 7;
            }else{
                Debug.tickMovement(ci, (ClientPlayerEntity) (Object) this, bl);
            }
        }
    }

    @Inject(method="sendMovementPackets", at=@At("HEAD"))
    private void sendMovementPackets(CallbackInfo ci) {
        if(((PlayerProperties) this).isHooked()){
            NetworkHandler.sendToServer(new UpdateInputMessage(this.jumping));
        }
    }

}
