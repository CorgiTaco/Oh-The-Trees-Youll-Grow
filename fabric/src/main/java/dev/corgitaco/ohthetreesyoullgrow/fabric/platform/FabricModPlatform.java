package dev.corgitaco.ohthetreesyoullgrow.fabric.platform;

import com.google.auto.service.AutoService;
import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

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
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        T value1 = Registry.register(registry, Constants.createLocation(name), value.get());
        return () -> value1;
    }
}
