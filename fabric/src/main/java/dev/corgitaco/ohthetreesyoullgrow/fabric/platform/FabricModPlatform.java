package dev.corgitaco.ohthetreesyoullgrow.fabric.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.nio.file.Path;
import java.util.function.Supplier;

@AutoService(ModPlatform.class)
public class FabricModPlatform implements ModPlatform {
    public static final Event<TagsUpdatedEvent> TAGS_UPDATED_EVENT = EventFactory.createArrayBacked(TagsUpdatedEvent.class, callbacks -> world -> {
        for (final var sub : callbacks) {
            sub.onTagsUpdated(world);
        }
    });

    @Override
    public Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID);
    }

    @Override
    public boolean isModLoaded(String isLoaded) {
        return FabricLoader.getInstance().isModLoaded(isLoaded);
    }

    @Override
    public String tagNameSpace() {
        return "c";
    }

    @Override
    public boolean isClientEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public Platform modPlatform() {
        return Platform.FABRIC;
    }

    @Override
    public boolean hasLoadErrors() {
        return false;
    }

    @Override
    public boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public String curseForgeURL() {
        return "https://curseforge.com/minecraft/mc-mods/oh-the-trees-youll-grow";
    }

    @Override
    public void addTagsUpdatedListener(TagsUpdatedEvent event) {
        TAGS_UPDATED_EVENT.register(event);
    }

    @Override
    public boolean canTreeGrowWithEvent(Level level, RandomSource source, BlockPos pos) {
        return true;
    }

    @Override
    public <FC extends FeatureConfiguration, T extends Feature<FC>> Supplier<T> registerFeature(Supplier<T> feature, String name) {
        T temp = Registry.register(BuiltInRegistries.FEATURE, Constants.createLocation(name), feature.get());
        return () -> temp;
    }

    @Override
    public <P extends TreeDecorator> Supplier<TreeDecoratorType<P>> registerTreeDecoratorType(Supplier<Codec<P>> codec, String name) {
        TreeDecoratorType<P> temp = Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, Constants.createLocation(name), new TreeDecoratorType<>(codec.get()));
        return () -> temp;
    }
}
