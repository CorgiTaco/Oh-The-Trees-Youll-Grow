package dev.corgitaco.ohthetreesyoullgrow.platform;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface ModPlatform {

    ModPlatform INSTANCE = Util.make(() -> {
        final var loader = ServiceLoader.load(ModPlatform.class);
        final var it = loader.iterator();
        if (!it.hasNext()) {
            throw new RuntimeException("No Mod Platform was found on the classpath!");
        } else {
            final ModPlatform factory = it.next();
            if (it.hasNext()) {
                throw new RuntimeException("More than one Mod Platform was found on the classpath!");
            }
            return factory;
        }
    });

    Path configPath();

    boolean isModLoaded(String isLoaded);


    String tagNameSpace();

    String curseForgeURL();

    boolean isClientEnvironment();

    Platform modPlatform();

    boolean hasLoadErrors();

    boolean isDevEnvironment();

    void addTagsUpdatedListener(TagsUpdatedEvent event);

    boolean canTreeGrowWithEvent(Level level, RandomSource source, BlockPos pos);

    @FunctionalInterface
    interface TagsUpdatedEvent {
        void onTagsUpdated(RegistryAccess access);
    }

    <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value);

    enum Platform {
        FORGE,
        FABRIC,
        NEOFORGE
    }
}
