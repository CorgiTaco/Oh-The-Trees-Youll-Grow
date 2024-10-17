package dev.corgitaco.ohthetreesyoullgrow.neoforge;

import dev.corgitaco.ohthetreesyoullgrow.CommonClass;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.neoforge.platform.NeoForgeModPlatform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class OhTheTreesYoullGrowNeoForge {
    public OhTheTreesYoullGrowNeoForge(final IEventBus bus) {
        CommonClass.init();
        NeoForgeModPlatform.CACHED.values().forEach(deferredRegister -> deferredRegister.register(bus));
    }
}