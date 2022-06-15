package net.jptrzy.mining.helmet;

import net.jptrzy.mining.helmet.block.AbstractHiddenOreBlock;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
