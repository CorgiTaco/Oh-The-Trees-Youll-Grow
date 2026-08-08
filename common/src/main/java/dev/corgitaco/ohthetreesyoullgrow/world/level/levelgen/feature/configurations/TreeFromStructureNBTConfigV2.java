package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeLogFilterBehavior;

import javax.annotation.Nullable;
import java.util.*;

public record TreeFromStructureNBTConfigV2(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                           IntProvider height,
                                           BlockStateProvider logProvider, List<BlockStateProvider> leavesProvider,
                                           Set<Block> logTarget, List<Block> leavesTarget,
                                           BlockPredicate growableOn, BlockPredicate leavesPlacementFilter,
                                           BlockPredicate logsPlacementFilter,
                                           TreeLogFilterBehavior treeLogFilterBehavior,
                                           int maxLogDepth,
                                           List<TreeDecorator> treeDecorators,
                                           Map<Block, BlockStateProvider> replaceFromNBT,
                                           boolean randomRotation,
                                           Orientation orientation) implements FeatureConfiguration {

    public static final Codec<Set<Block>> BLOCK_SET_CODEC = Codec.list(BuiltInRegistries.BLOCK.byNameCodec()).xmap(ObjectOpenHashSet::new, ArrayList::new);

    public static final Codec<TreeFromStructureNBTConfigV2> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("base_location").forGetter(TreeFromStructureNBTConfigV2::baseLocation),
                    ResourceLocation.CODEC.fieldOf("canopy_location").forGetter(TreeFromStructureNBTConfigV2::canopyLocation),
                    IntProvider.CODEC.fieldOf("height").forGetter(TreeFromStructureNBTConfigV2::height),
                    BlockStateProvider.CODEC.fieldOf("log_provider").forGetter(TreeFromStructureNBTConfigV2::logProvider),
                    BlockStateProvider.CODEC.listOf().fieldOf("leaves_provider").forGetter(TreeFromStructureNBTConfigV2::leavesProvider),
                    BLOCK_SET_CODEC.fieldOf("log_target").forGetter(TreeFromStructureNBTConfigV2::logTarget),
                    BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("leaves_target").forGetter(TreeFromStructureNBTConfigV2::leavesTarget),
                    BlockPredicate.CODEC.fieldOf("can_grow_on_filter").forGetter(TreeFromStructureNBTConfigV2::growableOn),
                    BlockPredicate.CODEC.fieldOf("can_leaves_place_filter").forGetter(TreeFromStructureNBTConfigV2::leavesPlacementFilter),
                    BlockPredicate.CODEC.optionalFieldOf("can_logs_place_filter", BlockPredicate.replaceable()).forGetter(TreeFromStructureNBTConfigV2::logsPlacementFilter),
                    TreeLogFilterBehavior.CODEC.optionalFieldOf("tree_filter_behavior", TreeLogFilterBehavior.BLOCK).forGetter(TreeFromStructureNBTConfigV2::treeLogFilterBehavior),
                    Codec.INT.optionalFieldOf("max_log_depth", 5).forGetter(TreeFromStructureNBTConfigV2::maxLogDepth),
                    TreeDecorator.CODEC.listOf().optionalFieldOf("decorators", new ArrayList<>()).forGetter(TreeFromStructureNBTConfigV2::treeDecorators),
                    Codec.unboundedMap(BuiltInRegistries.BLOCK.byNameCodec(), BlockStateProvider.CODEC).fieldOf("replace_from_nbt").forGetter(TreeFromStructureNBTConfigV2::replaceFromNBT),
                    Codec.BOOL.optionalFieldOf("random_rotation", true).forGetter(TreeFromStructureNBTConfigV2::randomRotation),
                    Orientation.CODEC.optionalFieldOf("orientation", Orientation.STANDARD).forGetter(TreeFromStructureNBTConfigV2::orientation)
            ).apply(builder, TreeFromStructureNBTConfigV2::new)
    );

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
        private List<BlockStateProvider> leavesProvider;
        @Nullable
        private Set<Block> logTarget;
        @Nullable
        private List<Block> leavesTarget;
        private BlockPredicate growableOn = BlockPredicate.replaceable();
        private BlockPredicate leavesPlacementFilter = BlockPredicate.replaceable();
        private BlockPredicate logsPlacementFilter = BlockPredicate.replaceable();
        private TreeLogFilterBehavior treeLogFilterBehavior = TreeLogFilterBehavior.BLOCK;
        private int maxLogDepth = 5;
        private List<TreeDecorator> treeDecorators = new ArrayList<>();
        private Map<Block, BlockStateProvider> replaceFromNBT = new HashMap<>();
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

        public Builder leavesProvider(List<BlockStateProvider> leavesProvider) {
            this.leavesProvider = leavesProvider;
            return this;
        }

        public Builder logTarget(Set<Block> logTarget) {
            this.logTarget = logTarget;
            return this;
        }

        public Builder leavesTarget(List<Block> leavesTarget) {
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

        public Builder replaceFromNBT(Map<Block, BlockStateProvider> placeFromNBT) {
            this.replaceFromNBT = placeFromNBT;
            return this;
        }

        public Builder randomRotation(boolean randomRotation) {
            this.randomRotation = randomRotation;
            return this;
        }

        public Builder orientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public TreeFromStructureNBTConfigV2 build() {
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

            return new TreeFromStructureNBTConfigV2(
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
                    replaceFromNBT,
                    randomRotation,
                    orientation
            );
        }
    }
}
