package net.jptrzy.mining.helmet.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.integrations.trinkets.OptionalTrinket;
import net.jptrzy.mining.helmet.network.message.Message;
import net.jptrzy.mining.helmet.network.message.TryHookingMessage;
import net.jptrzy.mining.helmet.network.message.UpdateInputMessage;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkHandler {

    public static final Identifier GLOBAL_PACKET_ID = new Identifier(Main.MOD_ID, Main.MOD_ID);
    public static List<Class<? extends Message>> messages = new ArrayList<>();


    public static void init() {
        add(TryHookingMessage.class);
        add(UpdateInputMessage.class);

        ServerPlayNetworking.registerGlobalReceiver(GLOBAL_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
            int id = buf.readInt();

            Class<? extends Message> mess = messages.get(id);

            try {
                Method read = mess.getMethod("read", PacketByteBuf.class);

                Method onMessage =
                        mess.getMethod("onMessage", mess, MinecraftServer.class, ServerPlayerEntity.class);

                onMessage.invoke(null, read.invoke(null, buf), server, player);

            } catch (Exception e) {
                Main.LOGGER.error("[NH] Failed to execute onMessage. {}", e.toString());
            }
        });
    }

    protected static void add(Class<? extends Message> message){
        messages.add(message);
    }

    @Environment(EnvType.CLIENT)
    public static <M extends Message> void sendToServer(M message) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt( messages.indexOf(message.getClass()) );   //Write Id
        // For some reason when `message.write` it uses Message write and not M write
        try {
            message.getClass().getMethod("write", message.getClass(), PacketByteBuf.class)
                    .invoke(null, message, buf);
        } catch (Exception e) {
            Main.LOGGER.error("[NH] Failed to write data to buffer. {}", e.toString());
        }
        ClientPlayNetworking.send(GLOBAL_PACKET_ID, buf);
    }
}
