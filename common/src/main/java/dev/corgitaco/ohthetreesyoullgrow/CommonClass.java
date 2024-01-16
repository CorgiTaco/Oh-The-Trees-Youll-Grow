package dev.corgitaco.ohthetreesyoullgrow;

import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGConfiguredFeatures;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGPlacedFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;

public class CommonClass {

    public static void init() {
        TYGFeatures.register();
        TYGConfiguredFeatures.register();
        TYGPlacedFeatures.register();
    }
}