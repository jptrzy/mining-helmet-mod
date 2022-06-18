package net.jptrzy.mining.helmet.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface PlayerProperties {


    boolean isHooked();
    void setHooked(boolean hooked);

    BlockPos getHookedBlock();
    void setHookedBlock(BlockPos pos);

}
