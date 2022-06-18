package net.jptrzy.mining.helmet;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.jptrzy.mining.helmet.block.AbstractHiddenOreBlock;
import net.jptrzy.mining.helmet.registry.ItemRegister;
import net.jptrzy.mining.helmet.util.PlayerProperties;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
        Main.LOGGER.warn("DOUBLE JUMP {} {} {}", bl, player.input.jumping, player.getAbilities().flying);

        ClientPlayNetworking.send(Main.NETWORK_TRY_HOOKING_ID, new PacketByteBuf(Unpooled.buffer()));
    }

    public static void getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir){
        if(entity instanceof PlayerEntity player && ((PlayerProperties) player).isHooked()
                && !player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegister.GRAPPLE_PACK) ){
            ((PlayerProperties) player).setHooked(false);
        }
    }

    public static void tryHooking(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender){

        BlockHitResult hit = player.world.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, 16, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));

        Main.LOGGER.warn(hit.getType());
        if(hit.getType() == HitResult.Type.BLOCK){
            ((PlayerProperties) player).setHooked(true);
            ((PlayerProperties) player).setHookedBlock(hit.getBlockPos());
        }
    }

    public static void updateInput(float sidewaysSpeed, float forwardSpeed, boolean jumping, boolean sneaking, CallbackInfo ci){
        Main.LOGGER.warn("UPDATE {}", jumping);
    }
}
