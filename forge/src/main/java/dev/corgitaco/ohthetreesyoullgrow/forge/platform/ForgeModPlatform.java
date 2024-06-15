package dev.corgitaco.ohthetreesyoullgrow.forge.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.function.Supplier;

@AutoService(ModPlatform.class)
public class ForgeModPlatform implements ModPlatform {

    @Override
    public Path configPath() {
        return FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID);
    }

    @Override
    public boolean isModLoaded(String isLoaded) {
        return ModList.get().isLoaded(isLoaded);
    }


    @Override
    public String curseForgeURL() {
        return "";
    }

    @Override
    public boolean isClientEnvironment() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public Platform modPlatform() {
        return Platform.FORGE;
    }

    @Override
    public boolean hasLoadErrors() {
        return !ModLoader.isLoadingStateValid();
    }

    @Override
    public boolean isDevEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public String tagNameSpace() {
        return "forge";
    }

    @Override
    public void addTagsUpdatedListener(TagsUpdatedEvent onTagsUpdated) {
        MinecraftForge.EVENT_BUS.addListener((final net.minecraftforge.event.TagsUpdatedEvent event) -> onTagsUpdated.onTagsUpdated(event.getRegistryAccess()));
    }

    @Override
    public boolean canTreeGrowWithEvent(Level level, RandomSource source, BlockPos pos) {
        return !net.minecraftforge.event.ForgeEventFactory.blockGrowFeature(level, source, pos, null).getResult().equals(Event.Result.DENY);
    }

    @Override
    public <FC extends FeatureConfiguration, T extends Feature<FC>> Supplier<T> registerFeature(Supplier<T> feature, String name) {
        DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
        Supplier<T> hold = FEATURES.register(name, feature);
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        return hold;
    }

    @Override
    public <P extends TreeDecorator> Supplier<TreeDecoratorType<P>> registerTreeDecoratorType(Supplier<Codec<P>> codec, String name) {
        DeferredRegister<TreeDecoratorType<?>> FEATURES = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Constants.MOD_ID);
        Supplier<TreeDecoratorType<P>> hold = FEATURES.register(name, () -> new TreeDecoratorType<>(codec.get()));
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        return hold;
    }

}
