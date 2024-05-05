package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.treedecorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttachedToFruitLeavesDecorator extends AttachedToLeavesDecorator {

    public static final Codec<AttachedToFruitLeavesDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability),
            Codec.intRange(0, 16).fieldOf("exclusion_radius_xz").forGetter(decorator -> decorator.exclusionRadiusXZ),
            Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter(decorator -> decorator.exclusionRadiusY),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("leaves_block").forGetter(decorator -> decorator.leavesBlock),
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(decorator -> decorator.blockProvider),
            Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter(decorator -> decorator.requiredEmptyBlocks),
            ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(decorator -> decorator.directions)
    ).apply(instance, AttachedToFruitLeavesDecorator::new));


    private final Block leavesBlock;

    public AttachedToFruitLeavesDecorator(float probability, int exclusionRadiusXZ, int exclusionRadiusY, Block leavesBlock, BlockStateProvider blockProvider, int requiredEmptyBlocks, List<Direction> directions) {
        super(probability, exclusionRadiusXZ, exclusionRadiusY, blockProvider, requiredEmptyBlocks, directions);
        this.leavesBlock = leavesBlock;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TYGTreeDecoratorTypes.ATTACHED_TO_FRUIT_LEAVES.get();
    }

    @Override
    public void place(@NotNull Context context) {
        Set<BlockPos> positions = new HashSet<>();
        RandomSource random = context.random();
        List<BlockPos> leavesPositions = new ArrayList<>();

        for (BlockPos pos : context.leaves()) {
            if (context.level().isStateAtPosition(pos, state -> state.is(this.leavesBlock))) {
                leavesPositions.add(pos);
            }
        }

        List<BlockPos> shuffledLeaves = Util.shuffledCopy(leavesPositions.toArray(new BlockPos[0]), random);

        for (BlockPos pos : shuffledLeaves) {
            Direction direction = Util.getRandom(this.directions, random);
            BlockPos relativePos = pos.relative(direction);

            if (positions.contains(relativePos)) {
                continue;
            }

            if (!(random.nextFloat() < this.probability)) {
                continue;
            }

            if (!this.hasRequiredEmptyBlocks(context, pos, direction)) {
                continue;
            }

            BlockPos minXZ = relativePos.offset(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ);
            BlockPos maxXZ = relativePos.offset(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ);

            for (BlockPos blockPos : BlockPos.betweenClosed(minXZ, maxXZ)) {
                positions.add(blockPos.immutable());
            }

            context.setBlock(relativePos, this.blockProvider.getState(random, relativePos));
        }
    }


    private boolean hasRequiredEmptyBlocks(TreeDecorator.Context context, BlockPos pos, Direction direction) {
        for (int i = 1; i <= this.requiredEmptyBlocks; i++) {
            BlockPos relativePos = pos.relative(direction, i);

            if (!context.isAir(relativePos)) {
                return false;
            }
        }

        return true;
    }
}