package dev.corgitaco.ohthetreesyoullgrow;

import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TreeFromStructureNBTFeature;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class OhTheTreesYoullGrowFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        registerFeature();
        CommonClass.init();
    }

    private void registerFeature() {
        TreeFromStructureNBTFeature temp = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Constants.MOD_ID, "tree_from_nbt_v1"),
                new TreeFromStructureNBTFeature(TreeFromStructureNBTConfig.CODEC.stable()));
        TYGFeatures.TREE_FROM_NBT_V1 = () -> temp;
    }
}
