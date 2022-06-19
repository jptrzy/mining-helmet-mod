package net.jptrzy.mining.helmet.network.message;

import net.jptrzy.mining.helmet.Debug;
import net.jptrzy.mining.helmet.Main;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class TryHookingMessage extends Message{

    public static TryHookingMessage read(PacketByteBuf buffer) {
        return new TryHookingMessage();
    }

    public static void write(TryHookingMessage message, PacketByteBuf buffer){
    };

    public static void onMessage(TryHookingMessage message, MinecraftServer server, ServerPlayerEntity player){
        server.execute(() -> {
            Debug.tryHooking(server, player);
        });
    };
}
