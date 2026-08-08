package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature;


import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfigV2;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

public class TYGFeatures {
    public static final Supplier<Feature<TreeFromStructureNBTConfig>> TREE_FROM_NBT_V1 = ModPlatform.INSTANCE.register(BuiltInRegistries.FEATURE, "tree_from_nbt_v1", () -> new TreeFromStructureNBTFeature(TreeFromStructureNBTConfig.CODEC.stable()));
    public static final Supplier<Feature<TreeFromStructureNBTConfigV2>> TREE_FROM_NBT_V2 = ModPlatform.INSTANCE.register(BuiltInRegistries.FEATURE, "tree_from_nbt_v2", () -> new TreeFromStructureNBTFeatureV2(TreeFromStructureNBTConfigV2.CODEC.stable()));

    public static void register() {}
}
