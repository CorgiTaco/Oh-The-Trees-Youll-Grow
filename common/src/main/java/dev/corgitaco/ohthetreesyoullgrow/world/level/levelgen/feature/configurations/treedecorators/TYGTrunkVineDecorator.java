package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.treedecorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

public class TYGTrunkVineDecorator extends TreeDecorator {

    public static final Codec<TYGTrunkVineDecorator> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    BuiltInRegistries.BLOCK.byNameCodec().flatXmap(TYGLeavesVineDecorator.mustExtendVineBlock(), TYGLeavesVineDecorator.mustExtendVineBlock()).fieldOf("vine_block").forGetter(bygLeavesVineDecorator -> bygLeavesVineDecorator.vineBlock),
                    Codec.FLOAT.fieldOf("probability").forGetter(bygLeavesVineDecorator -> bygLeavesVineDecorator.probability)
            ).apply(builder, ((vineBlock, probability) -> new TYGTrunkVineDecorator((VineBlock) vineBlock, probability)))
    );


    private final VineBlock vineBlock;
    private final float probability; // Vanilla is 0.66 chance.

    public TYGTrunkVineDecorator(VineBlock vineBlock, float probability) {
        this.vineBlock = vineBlock;
        this.probability = probability;
    }

    protected @NotNull TreeDecoratorType<?> type() {
        return TYGTreeDecoratorTypes.TRUNK_VINE.get();
    }

    public void place(TreeDecorator.Context context) {
        RandomSource randomSource = context.random();
        context.logs().forEach((pos) -> {
            BlockPos pos1;
            if (randomSource.nextFloat() <= this.probability) {
                pos1 = pos.west();
                if (context.isAir(pos1)) {
                    placeVine(context, pos1, VineBlock.EAST);
                }
            }

            if (randomSource.nextFloat() <= this.probability) {
                pos1 = pos.east();
                if (context.isAir(pos1)) {
                    placeVine(context, pos1, VineBlock.WEST);
                }
            }

            if (randomSource.nextFloat() <= this.probability) {
                pos1 = pos.north();
                if (context.isAir(pos1)) {
                    placeVine(context, pos1, VineBlock.SOUTH);
                }
            }

            if (randomSource.nextFloat() <= this.probability) {
                pos1 = pos.south();
                if (context.isAir(pos1)) {
                    placeVine(context, pos1, VineBlock.NORTH);
                }
            }
        });
    }

    public void placeVine(Context context, BlockPos blockPos, BooleanProperty booleanProperty) {
        context.setBlock(blockPos, this.vineBlock.defaultBlockState().setValue(booleanProperty, true));
    }
}