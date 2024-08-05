package dev.corgitaco.ohthetreesyoullgrow.forge;

import dev.corgitaco.ohthetreesyoullgrow.CommonClass;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.forge.platform.ForgeModPlatform;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class OhTheTreesYoullGrowForge {

    public OhTheTreesYoullGrowForge() {
        CommonClass.init();
        ForgeModPlatform.CACHED.values().forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }
}