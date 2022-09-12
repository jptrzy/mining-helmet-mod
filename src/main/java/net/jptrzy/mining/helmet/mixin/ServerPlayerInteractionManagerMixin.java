package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.init.ModComponents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow @Final protected ServerPlayerEntity player;

    @Inject(method="setGameMode", at=@At("RETURN"), cancellable = true)
    public void setGameMode(GameMode gameMode, @Nullable GameMode previousGameMode, CallbackInfo ci) {
        // UnHook Player on GameMode change
        if (previousGameMode != null && previousGameMode.isSurvivalLike() && (gameMode.isCreative() || gameMode.getId() == 3)) {
            ModComponents.GRAPPLE_PACK.get(player).setHooked(false);
        }
    }
}
