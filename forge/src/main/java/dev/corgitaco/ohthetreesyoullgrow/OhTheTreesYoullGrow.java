package dev.corgitaco.ohthetreesyoullgrow;

import dev.corgitaco.ohthetreesyoullgrow.platform.ForgeModPlatform;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class OhTheTreesYoullGrow {

    public OhTheTreesYoullGrow() {
        ForgeModPlatform.registerFeatures();
        CommonClass.init();
    }

}