package net.jptrzy.mining.helmet.mixin;

import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.jptrzy.mining.helmet.init.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method="getEquipmentLevel", at=@At("HEAD"))
    private static void getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir){
        // UnHook Player on unequipping GrapplePack
        if (entity instanceof PlayerEntity player) {
            GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(player);
            if (gpc.isHooked() && !player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.GRAPPLE_PACK)) {
                gpc.setHooked(false);
            }
        }
    }
}
