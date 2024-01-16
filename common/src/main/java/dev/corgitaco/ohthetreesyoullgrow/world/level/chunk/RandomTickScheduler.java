package dev.corgitaco.ohthetreesyoullgrow.world.level.chunk;


import net.minecraft.core.BlockPos;

import java.util.List;

public interface RandomTickScheduler {

    void scheduleRandomTick(BlockPos pos);


    List<BlockPos> getScheduledRandomTicks();
}
