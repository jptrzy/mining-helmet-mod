package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.block.AbstractHiddenOreBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpyglassItem.class)
public class SpyglassItemMixin extends Item {

    private SpyglassItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);

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
}
