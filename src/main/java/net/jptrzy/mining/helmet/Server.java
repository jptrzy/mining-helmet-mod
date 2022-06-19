package net.jptrzy.mining.helmet;

import net.fabricmc.api.DedicatedServerModInitializer;

public class Server implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Main.LOGGER.warn("ON START");
    }
}
