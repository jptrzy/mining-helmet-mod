package net.jptrzy.mining.helmet;

import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.jptrzy.mining.helmet.integrations.trinkets.MinerCharmTrinket;
import net.minecraft.entity.EntityType;

import static dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler;

public class Light implements DynamicLightsInitializer {
    @Override public void onInitializeDynamicLights() {
        registerDynamicLightHandler(EntityType.PLAYER, entity -> {
            if(Main.TRINKETS_LOADED){
                return MinerCharmTrinket.getLightLevel(entity);
            }
            return 0;
        });
    }
}
