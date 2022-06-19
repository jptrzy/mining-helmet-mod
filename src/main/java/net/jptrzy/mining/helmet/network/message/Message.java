package net.jptrzy.mining.helmet.network.message;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class Message{
    public static <M extends Message> M read(PacketByteBuf buffer) {
        Main.LOGGER.warn("'read' should have been overwrite");
        return null;
    }

    public static <M extends Message> void write(M message, PacketByteBuf buffer){
        Main.LOGGER.warn("'write' should have been overwrite");
    };

    public static <M extends Message> void onMessage(M message, MinecraftServer server, ServerPlayerEntity player){
        Main.LOGGER.warn("'onMessage' should have been overwrite");
    };
}
