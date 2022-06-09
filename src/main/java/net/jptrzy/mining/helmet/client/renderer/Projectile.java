package net.jptrzy.mining.helmet.client.renderer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.world.World;

public class Projectile extends ProjectileEntity {

    public Projectile(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    @Override
    protected void initDataTracker() {

    }
}
