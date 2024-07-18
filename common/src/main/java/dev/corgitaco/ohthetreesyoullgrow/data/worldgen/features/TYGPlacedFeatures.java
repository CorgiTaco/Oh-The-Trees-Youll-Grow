package dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TYGPlacedFeatures {
    public static final Map<ResourceKey<PlacedFeature>, PlacedFeatureFactory> PLACED_FEATURE_FACTORIES = new Reference2ObjectOpenHashMap<>();

    public static final ResourceKey<PlacedFeature> V1_TEST_TREE1 = createPlacedFeature("v1_test_tree1", TYGConfiguredFeatures.V1_TEST_TREE1, () -> CountPlacement.of(1));
    public static final ResourceKey<PlacedFeature> V1_TEST_TREE2 = createPlacedFeature("v1_test_tree2", TYGConfiguredFeatures.V1_TEST_TREE2, () -> CountPlacement.of(1));
    public static final ResourceKey<PlacedFeature> V1_TEST_TREE3 = createPlacedFeature("v1_test_tree3", TYGConfiguredFeatures.V1_TEST_TREE3, () -> CountPlacement.of(1));

    public static final ResourceKey<PlacedFeature> V1_TEST_MUSHROOM1 = createPlacedFeature("v1_test_mushroom1", TYGConfiguredFeatures.V1_TEST_MUSHROOM1, () -> CountPlacement.of(1));
    public static final ResourceKey<PlacedFeature> V1_TEST_MUSHROOM2 = createPlacedFeature("v1_test_mushroom2", TYGConfiguredFeatures.V1_TEST_MUSHROOM2, () -> CountPlacement.of(1));

    @SafeVarargs
    public static <FC extends FeatureConfiguration> ResourceKey<PlacedFeature> createPlacedFeature(String id, ResourceKey<ConfiguredFeature<?, ?>> feature, Supplier<PlacementModifier>... placementModifiers) {
        return createPlacedFeature(id, feature, () -> Arrays.stream(placementModifiers).map(Supplier::get).toList());
    }

    public static <FC extends FeatureConfiguration> ResourceKey<PlacedFeature> createPlacedFeature(String id, ResourceKey<ConfiguredFeature<?, ?>> feature, Supplier<List<PlacementModifier>> placementModifiers) {
        ResourceLocation location = Constants.createLocation(id);

        ResourceKey<PlacedFeature> placedFeatureKey = ResourceKey.create(Registries.PLACED_FEATURE, location);

        PLACED_FEATURE_FACTORIES.put(placedFeatureKey, configuredFeatureHolderGetter -> new PlacedFeature(configuredFeatureHolderGetter.getOrThrow(feature), placementModifiers.get()));

        return placedFeatureKey;
    }

    public static void register() {
    }

    @FunctionalInterface
    public interface PlacedFeatureFactory {
        PlacedFeature generate(HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureHolderGetter);
    }
}
