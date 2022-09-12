package net.jptrzy.mining.helmet.init;

import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.integrations.trinkets.GrapplePackTrinket;
import net.jptrzy.mining.helmet.integrations.trinkets.MinerCharmTrinket;
import net.jptrzy.mining.helmet.integrations.trinkets.OptionalTrinket;
import net.jptrzy.mining.helmet.item.GrapplePackItem;
import net.jptrzy.mining.helmet.item.MinerCharmItem;
import net.jptrzy.mining.helmet.item.MiningHelmet;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ModItems {

    public static Map<String, Item> items = new HashMap<>();
    public static Map<String, Class<? extends OptionalTrinket>> trinkets = new HashMap<>();

    public static final Item MINING_HELMET = add("mining_helmet",  new MiningHelmet());
    public static final Item MINER_CHARM = add("miner_charm",  new MinerCharmItem(), MinerCharmTrinket.class);
    public static final Item GRAPPLE_PACK = add("grapple_pack",  new GrapplePackItem(), GrapplePackTrinket.class);
    public static final Item RAW_ELDERIUM = add("raw_elderium", new Item(new Item.Settings().group(Main.ITEM_GROUP)));

    public static void init() {
        for ( Map.Entry<String, Item> entry : items.entrySet() ) {
            Registry.register(Registry.ITEM, Main.id(entry.getKey()), entry.getValue());
            if(Main.TRINKETS_LOADED && trinkets.containsKey(entry.getKey())){
                // Run Static Method (register) from class that extends Optional Trinkets
                try {
                    Method meth = trinkets.get(entry.getKey()).getMethod("register");
                    meth.invoke(null);
                } catch (Exception e) {
                    Main.LOGGER.error("Who could guest that it wouldn't work. {}", e.toString());
                }
            }
        }

        if(Main.TRINKETS_LOADED){
            MinerCharmTrinket.register();
            OptionalTrinket.register();
        }
    }

    public static Item add(String key, Item item){
        items.put(key, item);
        return item;
    }

    public static Item add(String key, Item item, Class<? extends OptionalTrinket> trinket){
        trinkets.put(key, trinket);
        return add(key, item);
    }
}
