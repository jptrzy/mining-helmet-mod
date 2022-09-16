package net.jptrzy.mining.helmet.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
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
         /*
            EVENTS
            ID      DESC
            42000   discover hidden ore
         */

        switch(eventId){
            case 42000:
                ParticleUtil.spawnParticle(world, pos, ParticleTypes.GLOW, UniformIntProvider.create(3, 5));
                break;
        }
    }

    @Inject(method="renderChunkDebugInfo", at=@At("RETURN"))
    private void renderChunkDebugInfo(Camera camera, CallbackInfo ci) {
        GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(MinecraftClient.getInstance().player);
        if (Main.DEBUG && gpc.isHooked()) {

            BlockPos pos = gpc.getHookedBlockPos();
            String info = "Dis " + String.format("%.5g%n", pos.getY() - MinecraftClient.getInstance().player.getY()) + "|";

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.SRC_ALPHA,
                    GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SrcFactor.ONE,
                    GlStateManager.DstFactor.ZERO
            );

            DebugRenderer.drawBox(pos, .01F, .1F, 1, .1F, .5F);

            DebugRenderer.drawString(info, pos.getX(), pos.getY() - 1, pos.getZ(), 255);
        }
    }
}
