package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.treedecorators;

import com.mojang.serialization.Codec;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.Supplier;

public class TYGTreeDecoratorTypes {

    public static final Supplier<TreeDecoratorType<TYGTrunkVineDecorator>> TRUNK_VINE = register("trunk_vine", TYGTrunkVineDecorator.CODEC::stable);
    public static final Supplier<TreeDecoratorType<TYGLeavesVineDecorator>> LEAVE_VINE = register("leave_vine", TYGLeavesVineDecorator.CODEC::stable);
    public static final Supplier<TreeDecoratorType<AttachedToLogsDecorator>> ATTACHED_TO_LOGS = register("attached_to_logs", AttachedToLogsDecorator.CODEC::stable);
    public static final Supplier<TreeDecoratorType<AttachedToFruitLeavesDecorator>> ATTACHED_TO_FRUIT_LEAVES = register("attached_to_fruit_leaves", AttachedToFruitLeavesDecorator.CODEC::stable);

    private static <P extends TreeDecorator> Supplier<TreeDecoratorType<P>> register(String id, Supplier<Codec<P>> codec) {
        return ModPlatform.INSTANCE.registerTreeDecoratorType(codec, id);
    }

    public static void register() {
    }

}
