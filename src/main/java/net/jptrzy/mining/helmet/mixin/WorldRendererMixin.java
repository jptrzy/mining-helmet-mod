package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.Debug;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow private ClientWorld world;

    @Inject(method="processWorldEvent", at=@At("TAIL"))
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo ci) {
        Debug.processWorldEvent(eventId, pos, data, ci, world);
    }

    @Inject(method="renderChunkDebugInfo", at=@At("RETURN"))
    private void renderChunkDebugInfo(Camera camera, CallbackInfo ci) {
        Debug.renderChunkDebugInfo(camera, ci);
    }
}
