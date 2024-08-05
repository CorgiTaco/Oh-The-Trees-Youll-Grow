package dev.corgitaco.ohthetreesyoullgrow.forge.data;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGConfiguredFeatures;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public class TYGDataProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> TYGConfiguredFeatures.CONFIGURED_FEATURES_FACTORIES.forEach((configuredFeatureResourceKey, configuredFeatureFactory) -> context.register(configuredFeatureResourceKey, configuredFeatureFactory.generate(context))))
            .add(Registries.PLACED_FEATURE, context -> TYGPlacedFeatures.PLACED_FEATURE_FACTORIES.forEach((placedFeatureResourceKey, placedFeatureFactory) -> context.register(placedFeatureResourceKey, placedFeatureFactory.generate(context.lookup(Registries.CONFIGURED_FEATURE)))));

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        final DataGenerator gen = event.getGenerator();
        gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(gen.getPackOutput(), event.getLookupProvider(), BUILDER, Set.of(Constants.MOD_ID)));
    }
}
