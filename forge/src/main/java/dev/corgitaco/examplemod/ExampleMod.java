package dev.corgitaco.examplemod;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ExampleMod {

    public ExampleMod() {
        CommonClass.init();
    }
}