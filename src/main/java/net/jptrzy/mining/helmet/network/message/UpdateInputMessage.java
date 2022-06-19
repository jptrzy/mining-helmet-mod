package net.jptrzy.mining.helmet.network.message;

import net.jptrzy.mining.helmet.Debug;
import net.jptrzy.mining.helmet.Main;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class UpdateInputMessage extends Message{

    private final boolean jumping;

    public UpdateInputMessage(boolean jumping) {
        this.jumping = jumping;
    }


    public static UpdateInputMessage read(PacketByteBuf buffer) {
        return new UpdateInputMessage(buffer.readBoolean());
    }

    public static void write(UpdateInputMessage message, PacketByteBuf buffer){
        buffer.writeBoolean(message.jumping);
    };

    public static void onMessage(UpdateInputMessage message, MinecraftServer server, ServerPlayerEntity player){
        server.execute(() -> {
            player.jumping = message.jumping;
        });
    };
}
