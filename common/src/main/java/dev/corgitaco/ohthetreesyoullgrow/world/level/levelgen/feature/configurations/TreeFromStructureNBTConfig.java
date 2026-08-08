package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public record TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                         IntProvider height,
                                         BlockStateProvider logProvider, BlockStateProvider leavesProvider,
                                         Set<Block> logTarget, Set<Block> leavesTarget,
                                         BlockPredicate growableOn, BlockPredicate leavesPlacementFilter,
                                         BlockPredicate logsPlacementFilter,
                                         TreeLogFilterBehavior treeLogFilterBehavior,
                                         int maxLogDepth,
                                         List<TreeDecorator> treeDecorators,
                                         Set<Block> placeFromNBT,
                                         boolean randomRotation,
                                         Orientation orientation) implements FeatureConfiguration {

    public static final Codec<Set<Block>> BLOCK_SET_CODEC = Codec.list(BuiltInRegistries.BLOCK.byNameCodec()).xmap(ObjectOpenHashSet::new, ArrayList::new);

    public static final Codec<TreeFromStructureNBTConfig> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("base_location").forGetter(TreeFromStructureNBTConfig::baseLocation),
                    ResourceLocation.CODEC.fieldOf("canopy_location").forGetter(TreeFromStructureNBTConfig::canopyLocation),
                    IntProvider.CODEC.fieldOf("height").forGetter(TreeFromStructureNBTConfig::height),
                    BlockStateProvider.CODEC.fieldOf("log_provider").forGetter(TreeFromStructureNBTConfig::logProvider),
                    BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(TreeFromStructureNBTConfig::leavesProvider),
                    BLOCK_SET_CODEC.fieldOf("log_target").forGetter(TreeFromStructureNBTConfig::logTarget),
                    BLOCK_SET_CODEC.fieldOf("leaves_target").forGetter(TreeFromStructureNBTConfig::leavesTarget),
                    BlockPredicate.CODEC.fieldOf("can_grow_on_filter").forGetter(TreeFromStructureNBTConfig::growableOn),
                    BlockPredicate.CODEC.fieldOf("can_leaves_place_filter").forGetter(TreeFromStructureNBTConfig::leavesPlacementFilter),
                    BlockPredicate.CODEC.optionalFieldOf("can_logs_place_filter", BlockPredicate.replaceable()).forGetter(TreeFromStructureNBTConfig::logsPlacementFilter),
                    TreeLogFilterBehavior.CODEC.optionalFieldOf("tree_filter_behavior", TreeLogFilterBehavior.BLOCK).forGetter(TreeFromStructureNBTConfig::treeLogFilterBehavior),
                    Codec.INT.optionalFieldOf("max_log_depth", 5).forGetter(TreeFromStructureNBTConfig::maxLogDepth),
                    TreeDecorator.CODEC.listOf().optionalFieldOf("decorators", new ArrayList<>()).forGetter(TreeFromStructureNBTConfig::treeDecorators),
                    BLOCK_SET_CODEC.fieldOf("place_from_nbt").forGetter(TreeFromStructureNBTConfig::placeFromNBT),
                    Codec.BOOL.optionalFieldOf("random_rotation", true).forGetter(TreeFromStructureNBTConfig::randomRotation),
                    Orientation.CODEC.optionalFieldOf("orientation", Orientation.STANDARD).forGetter(TreeFromStructureNBTConfig::orientation)
            ).apply(builder, TreeFromStructureNBTConfig::new)
    );

    @Deprecated(forRemoval = true, since = "Use Builder")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Collection<Block> logTarget,
                                      List<Block> leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, new ObjectOpenHashSet<>(logTarget), new ObjectOpenHashSet<>(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), BlockPredicate.replaceable(), TreeLogFilterBehavior.BLOCK, maxLogDepth, treeDecorators, Set.of(), true, Orientation.STANDARD);
    }

    @Deprecated(forRemoval = true, since = "Use Builder")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Block logTarget,
                                      Block leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, Collections.singleton(logTarget), Collections.singleton(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), BlockPredicate.replaceable(), TreeLogFilterBehavior.BLOCK, maxLogDepth, treeDecorators, Set.of(), true, Orientation.STANDARD);
    }

    @Deprecated(forRemoval = true, since = "Use Builder")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Block logTarget,
                                      Block leavesTarget, TagKey<Block> growableOn, int maxLogDepth) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, Collections.singleton(logTarget), Collections.singleton(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), BlockPredicate.replaceable(), TreeLogFilterBehavior.BLOCK, maxLogDepth, ImmutableList.of(), Set.of(), true, Orientation.STANDARD);
    }

    @Deprecated(forRemoval = true, since = "Use Builder")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Supplier<? extends Block> logTarget,
                                      Supplier<? extends Block> leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, logTarget.get(), leavesTarget.get(), growableOn, maxLogDepth, treeDecorators);
    }

    @Deprecated(forRemoval = true, since = "Use Builder class")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Collection<Block> logTarget,
                                      List<Block> leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators, boolean isSapling) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, new ObjectOpenHashSet<>(logTarget), new ObjectOpenHashSet<>(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), BlockPredicate.replaceable(), TreeLogFilterBehavior.BLOCK, maxLogDepth, treeDecorators, Set.of(), true, Orientation.STANDARD);
    }

    @Deprecated(forRemoval = true, since = "Use Builder class")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Block logTarget,
                                      Block leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators, boolean isSapling) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, Collections.singleton(logTarget), Collections.singleton(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), BlockPredicate.replaceable(), TreeLogFilterBehavior.BLOCK, maxLogDepth, treeDecorators, Set.of(), true, Orientation.STANDARD);
    }

    @Deprecated(forRemoval = true, since = "Use Builder class")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Block logTarget,
                                      Block leavesTarget, TagKey<Block> growableOn, int maxLogDepth, boolean isSapling) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, Collections.singleton(logTarget), Collections.singleton(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), BlockPredicate.replaceable(), TreeLogFilterBehavior.BLOCK, maxLogDepth, ImmutableList.of(), Set.of(), true, Orientation.STANDARD);
    }

    @Deprecated(forRemoval = true, since = "Use Builder class")
    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Supplier<? extends Block> logTarget,
                                      Supplier<? extends Block> leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators, boolean isSapling) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, logTarget.get(), leavesTarget.get(), growableOn, maxLogDepth, treeDecorators, isSapling);
    }

    public enum Orientation {
        STANDARD,
        UPSIDE_DOWN,
        SIDEWAYS;

        public static final Codec<Orientation> CODEC = Codec.STRING.xmap(s -> Orientation.valueOf(s.toUpperCase()), s -> s.name().toUpperCase()); // Guards against case issues
    }

    public static class Builder {
        @Nullable
        private ResourceLocation baseLocation;
        @Nullable
        private ResourceLocation canopyLocation;
        @Nullable
        private IntProvider height;
        @Nullable
        private BlockStateProvider logProvider;
        @Nullable
        private BlockStateProvider leavesProvider;
        @Nullable
        private Set<Block> logTarget;
        @Nullable
        private Set<Block> leavesTarget;
        private BlockPredicate growableOn = BlockPredicate.replaceable();
        private BlockPredicate leavesPlacementFilter = BlockPredicate.replaceable();
        private BlockPredicate logsPlacementFilter = BlockPredicate.replaceable();
        private TreeLogFilterBehavior treeLogFilterBehavior = TreeLogFilterBehavior.BLOCK;
        private int maxLogDepth = 5;
        private List<TreeDecorator> treeDecorators = new ArrayList<>();
        private Set<Block> placeFromNBT = new HashSet<>();
        private boolean randomRotation = true;
        private Orientation orientation = Orientation.STANDARD;

        public Builder baseLocation(ResourceLocation baseLocation) {
            this.baseLocation = baseLocation;
            return this;
        }

        public Builder canopyLocation(ResourceLocation canopyLocation) {
            this.canopyLocation = canopyLocation;
            return this;
        }

        public Builder height(IntProvider height) {
            this.height = height;
            return this;
        }

        public Builder logProvider(BlockStateProvider logProvider) {
            this.logProvider = logProvider;
            return this;
        }

        public Builder leavesProvider(BlockStateProvider leavesProvider) {
            this.leavesProvider = leavesProvider;
            return this;
        }

        public Builder logTarget(Set<Block> logTarget) {
            this.logTarget = logTarget;
            return this;
        }

        public Builder leavesTarget(Set<Block> leavesTarget) {
            this.leavesTarget = leavesTarget;
            return this;
        }

        public Builder growableOn(BlockPredicate growableOn) {
            this.growableOn = growableOn;
            return this;
        }

        public Builder leavesPlacementFilter(BlockPredicate leavesPlacementFilter) {
            this.leavesPlacementFilter = leavesPlacementFilter;
            return this;
        }

        public Builder logsPlacementFilter(BlockPredicate logsPlacementFilter) {
            this.logsPlacementFilter = logsPlacementFilter;
            return this;
        }

        public Builder treeLogFilterBehavior(TreeLogFilterBehavior treeLogFilterBehavior) {
            this.treeLogFilterBehavior = treeLogFilterBehavior;
            return this;
        }

        public Builder maxLogDepth(int maxLogDepth) {
            this.maxLogDepth = maxLogDepth;
            return this;
        }

        public Builder treeDecorators(List<TreeDecorator> treeDecorators) {
            this.treeDecorators = treeDecorators;
            return this;
        }

        public Builder placeFromNBT(Set<Block> placeFromNBT) {
            this.placeFromNBT = placeFromNBT;
            return this;
        }

        public Builder orientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder randomRotation(boolean randomRotation) {
            this.randomRotation = randomRotation;
            return this;
        }


        public TreeFromStructureNBTConfig build() {
            if (baseLocation == null) {
                throw new IllegalStateException("Base location cannot be null");
            }
            if (canopyLocation == null) {
                throw new IllegalStateException("Canopy location cannot be null");
            }
            if (height == null) {
                throw new IllegalStateException("Height cannot be null");
            }
            if (logProvider == null) {
                throw new IllegalStateException("Log provider cannot be null");
            }
            if (leavesProvider == null) {
                throw new IllegalStateException("Leaves provider cannot be null");
            }
            if (logTarget == null) {
                throw new IllegalStateException("Log target cannot be null");
            }
            if (leavesTarget == null) {
                throw new IllegalStateException("Leaves target cannot be null");
            }

            return new TreeFromStructureNBTConfig(
                    baseLocation,
                    canopyLocation,
                    height,
                    logProvider,
                    leavesProvider,
                    logTarget,
                    leavesTarget,
                    growableOn,
                    leavesPlacementFilter,
                    logsPlacementFilter,
                    treeLogFilterBehavior,
                    maxLogDepth,
                    treeDecorators,
                    placeFromNBT,
                    randomRotation,
                    orientation
            );
        }
    }
}