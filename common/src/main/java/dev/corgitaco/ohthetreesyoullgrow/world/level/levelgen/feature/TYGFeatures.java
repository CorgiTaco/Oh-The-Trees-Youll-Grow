package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature;


import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

public class TYGFeatures {
    public static Supplier<Feature<TreeFromStructureNBTConfig>> TREE_FROM_NBT_V1 = ModPlatform.INSTANCE.register(BuiltInRegistries.FEATURE, "tree_from_nbt_v1", () -> new TreeFromStructureNBTFeature(TreeFromStructureNBTConfig.CODEC.stable()));

    public static void register() {}
}
