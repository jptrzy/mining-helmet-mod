package net.jptrzy.mining.helmet.item;

import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.util.PlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

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

        if (entity instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.CHEST).equals(stack)
                && ((PlayerProperties) player).isHooked()) {

            int i = 0;
            if (player.jumping) {
                i++;
            }
            if (player.getFlag(1)) {
                i--;
            }

//            Main.LOGGER.warn(i);
            if (i != 0) {
                double y = player.getVelocity().getY();
                    if((i > 0) != (y > 0)){
                        y = 0;
                    }
                y = Math.max(-0.4, Math.min(0.4, y + i * .05));
                player.setVelocity(0, y, 0);
            } else {
                player.setVelocity(0, 0, 0);
            }

            BlockPos pos =  ((PlayerProperties) player).getHookedBlock();

            // UnHook Player if hooked block is ait or if landed
            if (player.isOnGround() || world.getBlockState(pos).isAir() || pos.getY()-player.getBlockPos().getY()<1 ) {
                ((PlayerProperties) player).setHooked(false);
                return;
            }

            // UnHook Player if blocks between player and hooked block arend air
            for (i=1; i<pos.getY()-player.getBlockPos().getY()-1; i++) {
                if(!world.getBlockState(pos.add(0,-i,0)).isAir()){
                    ((PlayerProperties) player).setHooked(false);
                    return;
                }
            }
        }
    }

//    public static boolean isEnabled(ItemStack stack){
//        NbtCompound tag = stack.getOrCreateNbt();
//        return tag != null && tag.contains("Enable") && tag.getBoolean("Enable");
//    }
//
//    public static void setEnabled(ItemStack stack, boolean enabled){
//        NbtCompound tag = stack.getOrCreateNbt();
//        tag.putBoolean("Enable", enabled);
//    }

    // UnHook Player on GameMode change
//    public static void onDeath(PlayerEntity player){
//        ((PlayerProperties) player).setHooked(false);
//    }
//
//    public static void onGameModeChange(PlayerEntity player){
//        ((PlayerProperties) player).setHooked(false);
//    }

}
