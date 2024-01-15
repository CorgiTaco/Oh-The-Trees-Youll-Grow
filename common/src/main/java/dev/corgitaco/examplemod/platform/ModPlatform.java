package dev.corgitaco.examplemod.platform;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.material.Fluid;

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

    SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties properties);

    MobBucketItem createMobBucketItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Item.Properties properties);

    CreativeModeTab creativeModeTab();

    enum Platform {
        FORGE,
        FABRIC
    }
}
