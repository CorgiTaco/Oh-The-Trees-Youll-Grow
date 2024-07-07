package dev.corgitaco.ohthetreesyoullgrow.fabric;

import dev.corgitaco.ohthetreesyoullgrow.CommonClass;
import net.fabricmc.api.ModInitializer;

public class OhTheTreesYoullGrowFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
