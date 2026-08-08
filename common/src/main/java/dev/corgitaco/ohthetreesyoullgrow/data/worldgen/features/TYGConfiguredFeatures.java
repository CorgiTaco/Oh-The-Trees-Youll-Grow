package dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfigV2;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class TYGConfiguredFeatures {
    public static final Map<ResourceKey<ConfiguredFeature<?, ?>>, ConfiguredFeatureFactory> CONFIGURED_FEATURES_FACTORIES = new Reference2ObjectOpenHashMap<>();

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_TREE1 = createConfiguredFeature("v1_test_tree_1", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.ACACIA_LOG))
                    .leavesProvider(BlockStateProvider.simple(Blocks.ACACIA_LEAVES))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .treeDecorators(List.of(new AlterGroundDecorator(SimpleStateProvider.simple(Blocks.MOSS_BLOCK))))
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_TREE2 = createConfiguredFeature("v1_test_tree_2", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.JUNGLE_LOG))
                    .leavesProvider(BlockStateProvider.simple(Blocks.JUNGLE_LEAVES))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(5)
                    .treeDecorators(List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F)))
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_TREE3 = createConfiguredFeature("v1_test_tree_3", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(20, 25))
                    .logProvider(BlockStateProvider.simple(Blocks.DIAMOND_BLOCK))
                    .leavesProvider(BlockStateProvider.simple(Blocks.EMERALD_BLOCK))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_UPSIDE_DOWN_TEST_TREE1 = createConfiguredFeature("v1_upside_down_test_tree1", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.ACACIA_LOG))
                    .leavesProvider(BlockStateProvider.simple(Blocks.ACACIA_LEAVES))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .treeDecorators(List.of(new AlterGroundDecorator(SimpleStateProvider.simple(Blocks.MOSS_BLOCK))))
                    .orientation(TreeFromStructureNBTConfig.Orientation.UPSIDE_DOWN)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_UPSIDE_DOWN_TEST_TREE2 = createConfiguredFeature("v1_upside_down_test_tree2", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.JUNGLE_LOG))
                    .leavesProvider(BlockStateProvider.simple(Blocks.JUNGLE_LEAVES))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(5)
                    .treeDecorators(List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F)))
                    .orientation(TreeFromStructureNBTConfig.Orientation.UPSIDE_DOWN)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_UPSIDE_DOWN_TEST_TREE3 = createConfiguredFeature("v1_upside_down_test_tree3", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(20, 25))
                    .logProvider(BlockStateProvider.simple(Blocks.DIAMOND_BLOCK))
                    .leavesProvider(BlockStateProvider.simple(Blocks.EMERALD_BLOCK))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .orientation(TreeFromStructureNBTConfig.Orientation.UPSIDE_DOWN)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_SIDEWAYS_TEST_TREE1 = createConfiguredFeature("v1_sideways_test_tree1", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.ACACIA_LOG))
                    .leavesProvider(BlockStateProvider.simple(Blocks.ACACIA_LEAVES))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .treeDecorators(List.of(new AlterGroundDecorator(SimpleStateProvider.simple(Blocks.MOSS_BLOCK))))
                    .orientation(TreeFromStructureNBTConfig.Orientation.SIDEWAYS)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_SIDEWAYS_TEST_TREE2 = createConfiguredFeature("v1_sideways_test_tree2", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.JUNGLE_LOG))
                    .leavesProvider(BlockStateProvider.simple(Blocks.JUNGLE_LEAVES))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(5)
                    .treeDecorators(List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F)))
                    .orientation(TreeFromStructureNBTConfig.Orientation.SIDEWAYS)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_SIDEWAYS_TEST_TREE3 = createConfiguredFeature("v1_sideways_test_tree3", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(20, 25))
                    .logProvider(BlockStateProvider.simple(Blocks.DIAMOND_BLOCK))
                    .leavesProvider(BlockStateProvider.simple(Blocks.EMERALD_BLOCK))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .orientation(TreeFromStructureNBTConfig.Orientation.SIDEWAYS)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_MUSHROOM1 = createConfiguredFeature("v1_test_mushroom_1", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_trunk1"))
                    .canopyLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.MUSHROOM_STEM))
                    .leavesProvider(BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK))
                    .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                    .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.MUSHROOM_GROW_BLOCK))
                    .maxLogDepth(3)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V1_TEST_MUSHROOM2 = createConfiguredFeature("v1_test_mushroom_2", TYGFeatures.TREE_FROM_NBT_V1, ctx ->
            new TreeFromStructureNBTConfig.Builder()
                    .baseLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_trunk1"))
                    .canopyLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.MUSHROOM_STEM))
                    .leavesProvider(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM_BLOCK))
                    .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                    .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.MUSHROOM_GROW_BLOCK))
                    .maxLogDepth(3)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> V2_TEST_TREE1 = createConfiguredFeature("v2_test_tree_1", TYGFeatures.TREE_FROM_NBT_V2, ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv2/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv2/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.simple(Blocks.ACACIA_LOG))
                    .leavesProvider(List.of(BlockStateProvider.simple(Blocks.OAK_LEAVES), BlockStateProvider.simple(Blocks.OAK_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.DIRT))
                    .maxLogDepth(3)
                    .replaceFromNBT(Map.of(Blocks.SHROOMLIGHT, new WeightedStateProvider(WeightedList.<BlockState>builder().add(Blocks.GLOWSTONE.defaultBlockState(), 3).add(Blocks.GLASS.defaultBlockState(), 1).build())))
                    .treeDecorators(List.of(new AlterGroundDecorator(SimpleStateProvider.simple(Blocks.MOSS_BLOCK))))
                    .build()
    );

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> ResourceKey<ConfiguredFeature<?, ?>> createConfiguredFeature(String id, Supplier<? extends F> feature, Function<BootstrapContext<ConfiguredFeature<?, ?>>, ? extends FC> config) {
        ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey = Constants.key(Registries.CONFIGURED_FEATURE, id);

        CONFIGURED_FEATURES_FACTORIES.put(configuredFeatureResourceKey, configuredFeatureHolderGetter -> new ConfiguredFeature<>(feature.get(), config.apply(configuredFeatureHolderGetter)));

        return configuredFeatureResourceKey;
    }

    public static void register() {
    }

    @FunctionalInterface
    public interface ConfiguredFeatureFactory {
        ConfiguredFeature<?, ?> generate(BootstrapContext<ConfiguredFeature<?, ?>> configuredFeatureHolderGetter);
    }
}