package dev.corgitaco.ohthetreesyoullgrow;

import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGConfiguredFeatures;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGPlacedFeatures;

public class CommonClass {

    public static void init() {
        TYGConfiguredFeatures.register();
        TYGPlacedFeatures.register();
    }
}