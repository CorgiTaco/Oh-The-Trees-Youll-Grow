package dev.corgitaco.ohthetreesyoullgrow;

import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGConfiguredFeatures;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGPlacedFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.treedecorators.TYGTreeDecoratorTypes;

public class CommonClass {

    public static void init() {
        TYGFeatures.register();
        TYGConfiguredFeatures.register();
        TYGPlacedFeatures.register();
        TYGTreeDecoratorTypes.register();
    }
}