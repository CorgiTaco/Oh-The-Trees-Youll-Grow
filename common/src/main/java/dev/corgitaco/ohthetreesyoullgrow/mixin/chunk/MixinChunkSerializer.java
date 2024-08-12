package dev.corgitaco.ohthetreesyoullgrow.mixin.chunk;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.RandomTickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ChunkSerializer.class)
public class MixinChunkSerializer {


    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;getBlendingData()Lnet/minecraft/world/level/levelgen/blending/BlendingData;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void writeScheduledRandomTicks(ServerLevel serverLevel, ChunkAccess chunkAccess, CallbackInfoReturnable<CompoundTag> cir, ChunkPos chunkPos, CompoundTag tag) {
        List<BlockPos> scheduledRandomTicks = ((RandomTickScheduler) chunkAccess).getScheduledRandomTicks();

        if (!scheduledRandomTicks.isEmpty()) {
            CompoundTag corgiLibTag = new CompoundTag();

            ListTag listTag = new ListTag();
            for (BlockPos scheduledRandomTick : scheduledRandomTicks) {
                listTag.add(NbtUtils.writeBlockPos(scheduledRandomTick));
            }
            corgiLibTag.put("scheduled_random_ticks", listTag);

            tag.put(Constants.MOD_ID, corgiLibTag);
        }
    }


    @Inject(method = "read", at = @At("RETURN"))
    private static void readScheduledRandomTicks(ServerLevel level, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir) {
        if (tag.contains(Constants.MOD_ID)) {
            CompoundTag corgiLibTag = tag.getCompound(Constants.MOD_ID);
            if (corgiLibTag.contains("scheduled_random_ticks", Tag.TAG_LIST)) {
                for (Tag scheduledTick : tag.getList("scheduled_random_ticks", Tag.TAG_COMPOUND)) {
                    int[] intArrayTag = ((IntArrayTag) scheduledTick).getAsIntArray();
                    ((RandomTickScheduler) cir.getReturnValue()).getScheduledRandomTicks().add(new BlockPos(intArrayTag[0], intArrayTag[1], intArrayTag[2]));
                }
            }
        }
    }
}
