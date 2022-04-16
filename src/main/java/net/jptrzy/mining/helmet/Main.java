package net.jptrzy.mining.helmet;

import net.fabricmc.api.ModInitializer;
import net.jptrzy.mining.helmet.item.MiningHelmet;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
//
	public static final String MOD_ID = "mining_helmet";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
//
	public static final Item MINING_HELMET = new MiningHelmet(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, id("mining_helmet"), MINING_HELMET);
	}

	public static Identifier id(String key){
		return new Identifier(MOD_ID, key);
	}
}
