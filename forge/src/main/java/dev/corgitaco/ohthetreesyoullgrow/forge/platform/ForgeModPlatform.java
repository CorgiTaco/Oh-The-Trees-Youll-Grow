package dev.corgitaco.ohthetreesyoullgrow.forge.platform;

import com.google.auto.service.AutoService;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;

import java.nio.file.Path;
import java.util.Map;
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
        return "https://www.curseforge.com/minecraft/mc-mods/oh-the-trees-youll-grow";
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

    public static final Map<ResourceKey<?>, DeferredRegister> CACHED = new Reference2ObjectOpenHashMap<>();

    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        return CACHED.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), Constants.MOD_ID)).register(name, value);
    }
}
