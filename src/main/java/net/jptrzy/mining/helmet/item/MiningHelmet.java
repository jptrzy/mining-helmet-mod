package net.jptrzy.mining.helmet.item;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class MiningHelmet extends ArmorItem {
    public MiningHelmet() {
        super(
            ArmorMaterials.GOLD,
            EquipmentSlot.HEAD,
            new Item.Settings().group(Main.ITEM_GROUP)
        );
    }
}
