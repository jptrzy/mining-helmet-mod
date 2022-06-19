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


        if(entity instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.CHEST).equals(stack)
                && ((PlayerProperties) player).isHooked()){
//            Main.LOGGER.warn("GrapplePack Working");
//            player.setVelocity(0, 0, 0);
//            player.setMovementSpeed(0);

            int i = 0;
            if (player.jumping) {
                i++;
            }
            if (player.getFlag(1)) {
                i--;
            }

//            Main.LOGGER.warn(i);
            if(i != 0){
                double y = player.getVelocity().getY();
                    if((i > 0) != (y > 0)){
                        y = 0;
                    }
                y = Math.max(-0.4, Math.min(0.4, y + i * .05));
                player.setVelocity(0, y, 0);
            }else{
                player.setVelocity(0, 0, 0);
            }


//
//            player.getFlag(1);
//
//            player.jum
            if(player.isOnGround()){
                ((PlayerProperties) player).setHooked(false);
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



}
