package net.jptrzy.mining.helmet;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.jptrzy.mining.helmet.block.AbstractHiddenOreBlock;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.jptrzy.mining.helmet.network.NetworkHandler;
import net.jptrzy.mining.helmet.network.message.TryHookingMessage;
import net.jptrzy.mining.helmet.init.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class Debug {

    /*
    EVENTS
    ID      DESC
    42000   discover hidden ore
     */

    public static void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks){
        HitResult hit = user.raycast(64, 0, false);
        BlockPos pos = ((BlockHitResult) hit).getBlockPos();

        if (hit.getType() == HitResult.Type.BLOCK && user instanceof PlayerEntity player) {
            if (world.getBlockState(pos).isOf(Main.ELDERIUM_ORE_BLOCK) && world.getBlockState(pos).get(AbstractHiddenOreBlock.STATE).equals(AbstractHiddenOreBlock.State.HIDDEN_ANIMATED) ){
                world.setBlockState(pos, world.getBlockState(pos).with(AbstractHiddenOreBlock.STATE, AbstractHiddenOreBlock.State.UNHIDDEN));

                world.syncWorldEvent(player, 42000, pos, 0);
                world.playSound(player, user.getBlockPos(), Main.FIND_HIDDEN_ORE_SOUND_EVENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    public static void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo ci, ClientWorld world) {
        switch(eventId){
            case 42000:
                ParticleUtil.spawnParticle(world, pos, ParticleTypes.GLOW, UniformIntProvider.create(3, 5));
            break;
        }
    }

    public static void tickMovement(CallbackInfo ci, ClientPlayerEntity player, boolean bl){
        NetworkHandler.sendToServer(new TryHookingMessage());
    }

    public static void getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir){
        // UnHook Player on unequipping GrapplePack
        if (entity instanceof PlayerEntity player) {
            GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(player);
            if (gpc.isHooked() && !player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.GRAPPLE_PACK)) {
                gpc.setHooked(false);
            }
        }
    }

    public static void tryHooking(MinecraftServer server, ServerPlayerEntity player){ //ServerPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender
        GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(player);

        if (!player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.GRAPPLE_PACK)) {
            return;
        }

        BlockHitResult hit = player.world.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, 16, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));

        if(player.isCreative()) {
            gpc.setHooked(false);
            return;
        }

        if (gpc.isHooked()) {
            gpc.setHooked(false);
        } else if (hit.getType() == HitResult.Type.BLOCK) {
            gpc.setHooked(true);
            gpc.setHookedBlockPos(hit.getBlockPos());
        }
    }

    public static void renderChunkDebugInfo(Camera camera, CallbackInfo ci) {
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
