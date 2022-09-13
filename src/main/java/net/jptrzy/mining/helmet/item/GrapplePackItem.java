package net.jptrzy.mining.helmet.item;

import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class GrapplePackItem extends ArmorItem {
    public GrapplePackItem() {
        super(
                ArmorMaterials.GOLD,
                EquipmentSlot.CHEST,
                new Item.Settings().group(Main.ITEM_GROUP)
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.CHEST).equals(stack)) {
            GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(entity);
            if (gpc.isHooked()) {
                int dir = 0;
                player.getMovementDirection();
                if (player.jumping) {
                    dir++;
                }
                if (player.getFlag(1)) {
                    dir--;
                }

                if (dir != 0) {
                    double y = player.getVelocity().getY();
                    if((dir > 0) != (y > 0)){
                        y = 0;
                    }
                    y = Math.max(-0.4, Math.min(0.4, y + dir * .05));
                    player.setVelocity(0, y, 0);
                } else {
                    player.setVelocity(0, 0, 0);
                }

                BlockPos pos = gpc.getHookedBlockPos();

                // UnHook Player if hooked block is ait or if landed
                if (player.isOnGround() || world.getBlockState(pos).isAir() || player.getBlockPos().getY() >= pos.getY() ) {
                    Main.LOGGER.info("Unhook - ground {} {}", pos, player.isOnGround());
                    gpc.setHooked(false);
                    return;
                }

                // UnHook Player if blocks between player and hooked block arend air
                for (int i=1; i<pos.getY()-player.getBlockPos().getY()-1; i++) {
                    if(!world.getBlockState(pos.add(0,-i,0)).isAir()){
                        gpc.setHooked(false);
                        return;
                    }
                }
            }
        }
    }
}
