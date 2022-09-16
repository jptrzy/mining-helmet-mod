package net.jptrzy.mining.helmet.network.message;

import net.jptrzy.mining.helmet.components.GrapplePackComponent;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.jptrzy.mining.helmet.init.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

public class TryHookingMessage extends Message{

    public static TryHookingMessage read(PacketByteBuf buffer) {
        return new TryHookingMessage();
    }

    public static void write(TryHookingMessage message, PacketByteBuf buffer){
    };

    public static void onMessage(TryHookingMessage message, MinecraftServer server, ServerPlayerEntity player){
        server.execute(() -> {
            GrapplePackComponent gpc = ModComponents.GRAPPLE_PACK.get(player);

            if (!player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.GRAPPLE_PACK)) {
                return;
            }

            if(player.isCreative() || player.hasVehicle()) {
                gpc.setHooked(false);
                return;
            }

            BlockHitResult hit = player.world.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, 16, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));

            if (gpc.isHooked()) {
                gpc.setHooked(false);
            } else if (hit.getType() == HitResult.Type.BLOCK) {
                gpc.setHooked(true);
                gpc.setHookedBlockPos(hit.getBlockPos());
            }
        });
    };
}
