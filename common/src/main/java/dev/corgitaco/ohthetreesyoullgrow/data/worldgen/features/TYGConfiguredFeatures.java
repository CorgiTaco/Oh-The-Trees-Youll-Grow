package dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class TYGConfiguredFeatures {
    public static final Map<ResourceKey<ConfiguredFeature<?, ?>>, ConfiguredFeatureFactory> CONFIGURED_FEATURES_FACTORIES = new Reference2ObjectOpenHashMap<>();

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_TREE1 = createConfiguredFeature("v1_test_tree_1",  TYGFeatures.TREE_FROM_NBT_V1, configuredFeatureBootstapContext -> new TreeFromStructureNBTConfig(
            Constants.createLocation("features/trees/testv1/test_tree_trunk1"),
            Constants.createLocation("features/trees/testv1/test_tree_canopy1"),
            UniformInt.of(5, 10),
            BlockStateProvider.simple(Blocks.ACACIA_LOG),
            BlockStateProvider.simple(Blocks.ACACIA_LEAVES),
            Blocks.OAK_LOG, Blocks.OAK_LEAVES, BlockTags.DIRT,
            3,
            List.of(new AlterGroundDecorator(SimpleStateProvider.simple(Blocks.MOSS_BLOCK)))
            ));

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_TREE2 = createConfiguredFeature("v1_test_tree_2", TYGFeatures.TREE_FROM_NBT_V1, configuredFeatureBootstapContext -> new TreeFromStructureNBTConfig(
            Constants.createLocation("features/trees/testv1/test_tree_trunk1"),
            Constants.createLocation("features/trees/testv1/test_tree_canopy1"),
            UniformInt.of(5, 10),
            BlockStateProvider.simple(Blocks.JUNGLE_LOG),
            BlockStateProvider.simple(Blocks.JUNGLE_LEAVES),
            Blocks.OAK_LOG, Blocks.OAK_LEAVES, BlockTags.DIRT,
            5,
            List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F))
    ));

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_TREE3 = createConfiguredFeature("v1_test_tree_3", TYGFeatures.TREE_FROM_NBT_V1, configuredFeatureBootstapContext -> new TreeFromStructureNBTConfig(
            Constants.createLocation("features/trees/testv1/test_tree_trunk1"),
            Constants.createLocation("features/trees/testv1/test_tree_canopy1"),
            UniformInt.of(20, 25),
            BlockStateProvider.simple(Blocks.DIAMOND_BLOCK),
            BlockStateProvider.simple(Blocks.EMERALD_BLOCK),
            Blocks.OAK_LOG, Blocks.OAK_LEAVES, BlockTags.DIRT,
            3,
            List.of()
    ));


    public static <FC extends FeatureConfiguration, F extends Feature<FC>> ResourceKey<ConfiguredFeature<?, ?>> createConfiguredFeature(String id, Supplier<? extends F> feature, Function<BootstapContext<ConfiguredFeature<?, ?>>, ? extends FC> config) {
        ResourceLocation bygID = Constants.createLocation(id);

        ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, bygID);

        CONFIGURED_FEATURES_FACTORIES.put(configuredFeatureResourceKey, configuredFeatureHolderGetter -> new ConfiguredFeature<>(feature.get(), config.apply(configuredFeatureHolderGetter)));

        return configuredFeatureResourceKey;
    }

    public static void register() {
    }

    @FunctionalInterface
    public interface ConfiguredFeatureFactory {
        ConfiguredFeature<?, ?> generate(BootstapContext<ConfiguredFeature<?, ?>> configuredFeatureHolderGetter);
    }
}
