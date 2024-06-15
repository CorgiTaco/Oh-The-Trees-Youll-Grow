package dev.corgitaco.ohthetreesyoullgrow.fabric.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluid;

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
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new SpawnEggItem(type.get(), backgroundColor, highlightColor, properties);
    }

    @Override
    public MobBucketItem createMobBucketItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Item.Properties properties) {
        return new MobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), properties);
    }

    @Override
    public CreativeModeTab creativeModeTab() {
        return FabricItemGroup.builder().title(Component.translatable("itemGroup." + Constants.MOD_ID + "." + Constants.MOD_ID)).icon(() -> new ItemStack(Items.POPPY)).displayItems((displayParameters, pOutput) -> {

        }).build();
    }

    @Override
    public <FC extends FeatureConfiguration, T extends Feature<FC>> Supplier<T> registerFeature(Supplier<T> feature, String name) {
        T temp = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Constants.MOD_ID, name), feature.get());
        return () -> temp;
    }

    @Override
    public <P extends TreeDecorator> Supplier<TreeDecoratorType<P>> registerTreeDecoratorType(Supplier<Codec<P>> codec, String name) {
        TreeDecoratorType<P> temp = Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, new ResourceLocation(Constants.MOD_ID, name), new TreeDecoratorType<>(codec.get()));
        return () -> temp;
    }

}
