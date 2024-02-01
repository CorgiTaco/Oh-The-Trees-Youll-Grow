package dev.corgitaco.ohthetreesyoullgrow;

import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TreeFromStructureNBTFeature;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

@Mod(Constants.MOD_ID)
public class OhTheTreesYoullGrow {

    public OhTheTreesYoullGrow() {
        registerFeature();
        CommonClass.init();
    }

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

    private void registerFeature() {
        TYGFeatures.TREE_FROM_NBT_V1 = FEATURES.register("tree_from_nbt_v1", () -> new TreeFromStructureNBTFeature(TreeFromStructureNBTConfig.CODEC.stable()));
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}