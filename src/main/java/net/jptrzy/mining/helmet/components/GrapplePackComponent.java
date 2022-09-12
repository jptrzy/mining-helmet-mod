package net.jptrzy.mining.helmet.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.init.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("UnstableApiUsage")
public class GrapplePackComponent implements PlayerComponent<Component>, AutoSyncedComponent, ServerTickingComponent {


    private boolean hooked;
    private BlockPos hookedBlock;

    private boolean dirty;
    private final PlayerEntity provider;

    public GrapplePackComponent(PlayerEntity provider) {
        this.provider = provider;
        this.hookedBlock = new BlockPos(0, 0, 0);
        this.hooked = false;
        this.dirty = true;
    }

    protected void makeDirty() {
        this.dirty = true;
    }

    public boolean isHooked() {
        return this.hooked;
    }
    public BlockPos getHookedBlockPos() {
        return this.hookedBlock;
    }

    public void setHooked(boolean hooked) {
//        Main.LOGGER.info("Change Hooked from {} to {}", this.hooked, hooked);
        this.makeDirty();
        this.hooked = hooked;
    }
    public void setHookedBlockPos(BlockPos pos) {
        this.makeDirty();
        this.hookedBlock = pos;
    }

    @Override public void readFromNbt(NbtCompound tag) {
        this.setHooked(tag.getBoolean("hooked"));
        this.setHookedBlockPos(
                NbtHelper.toBlockPos(tag.getCompound("hookedBlockPos"))
        );
    }

    @Override public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("hooked", this.isHooked());
        tag.put("hookedBlockPos",
                NbtHelper.fromBlockPos(this.getHookedBlockPos())
        );
    }

    @Override public boolean shouldSyncWith(ServerPlayerEntity player) {
//        return AutoSyncedComponent.super.shouldSyncWith(player);
        return player == provider;
    }

    @Override public void serverTick() {
        if (this.dirty) {
            ModComponents.GRAPPLE_PACK.sync(provider);
            this.dirty = false;
        }
    }
}
