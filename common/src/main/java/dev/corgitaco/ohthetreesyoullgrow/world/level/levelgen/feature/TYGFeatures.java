package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature;


import corgitaco.ohthetreesyoullgrow.reg.RegistrationProvider;
import corgitaco.ohthetreesyoullgrow.reg.RegistryObject;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public class TYGFeatures {

    private static final RegistrationProvider<Feature<?>> PROVIDER = RegistrationProvider.get(Registries.FEATURE, Constants.MOD_ID);

    public static final RegistryObject<Feature<TreeFromStructureNBTConfig>> TREE_FROM_NBT_V1 = createFeature("tree_from_nbt_v1", () -> new TreeFromStructureNBTFeature(TreeFromStructureNBTConfig.CODEC.stable()));


    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> createFeature(String id, Supplier<? extends F> feature) {
        return PROVIDER.register(id, feature);
    }

    public static void register() {
    }

}
