package net.jptrzy.mining.helmet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.jptrzy.mining.helmet.block.ElderiumOreBlock;
import net.jptrzy.mining.helmet.integrations.trinkets.MinerCharmTrinket;
import net.jptrzy.mining.helmet.item.MinerCharmItem;
import net.jptrzy.mining.helmet.item.MiningHelmet;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {

	public static final boolean DEBUG = true;

	public static final String MOD_ID = "mining_helmet";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static boolean TRINKETS_LOADED;

	// Items
	public static final Item MINING_HELMET = new MiningHelmet();
	public static final Item MINER_CHARM = new MinerCharmItem();

	// Blocks
	public static final Block ELDERIUM_ORE_BLOCK = new ElderiumOreBlock(FabricBlockSettings.of(Material.STONE).strength(4.0f).nonOpaque());

	// Sounds
	public static Identifier FIND_HIDDEN_ORE_SOUND_ID= id("entity.player.find.hidden.ore");
	public static SoundEvent FIND_HIDDEN_ORE_SOUND_EVENT = new SoundEvent(FIND_HIDDEN_ORE_SOUND_ID);


	@Override public void onInitialize() {
		TRINKETS_LOADED = FabricLoader.getInstance().isModLoaded("trinkets");

		if(TRINKETS_LOADED){
			MinerCharmTrinket.register();
		}

		Registry.register(Registry.ITEM, id("mining_helmet"), MINING_HELMET);
		Registry.register(Registry.ITEM, id("miner_charm"), MINER_CHARM);

		Registry.register(Registry.BLOCK, id("elderium_ore"), ELDERIUM_ORE_BLOCK);
		Registry.register(Registry.ITEM, id("elderium_ore"), new BlockItem(ELDERIUM_ORE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

		Registry.register(Registry.SOUND_EVENT, FIND_HIDDEN_ORE_SOUND_ID, FIND_HIDDEN_ORE_SOUND_EVENT);
	}

	public static Identifier id(String key){
		return new Identifier(MOD_ID, key);
	}
	public static float toRadiant(float degree){
		return degree * MathHelper.PI / 180F;
	}
}
