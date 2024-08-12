package dev.corgitaco.ohthetreesyoullgrow.neoforge;

import dev.corgitaco.ohthetreesyoullgrow.CommonClass;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.neoforge.platform.NeoForgeModPlatform;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class OhTheTreesYoullGrowNeoForge {
    public OhTheTreesYoullGrowNeoForge() {
        CommonClass.init();
        NeoForgeModPlatform.CACHED.values().forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }
}