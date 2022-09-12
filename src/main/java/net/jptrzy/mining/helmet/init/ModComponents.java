package net.jptrzy.mining.helmet.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.jptrzy.mining.helmet.Main;
import net.jptrzy.mining.helmet.components.GrapplePackComponent;

public class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<GrapplePackComponent> GRAPPLE_PACK =
            ComponentRegistryV3.INSTANCE.getOrCreate(Main.id("grapple_pack"), GrapplePackComponent.class);

    @Override public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(GRAPPLE_PACK, GrapplePackComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
