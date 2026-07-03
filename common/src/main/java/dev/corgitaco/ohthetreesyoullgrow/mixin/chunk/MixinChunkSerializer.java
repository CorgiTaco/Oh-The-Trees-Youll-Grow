package dev.corgitaco.ohthetreesyoullgrow.mixin.chunk;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.RandomTickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(SerializableChunkData.class)
public class MixinChunkSerializer {


    @Shadow
    @Final
    private CompoundTag structureData;

    @Inject(method = "copyOf", at = @At(value = "RETURN"))
    private static void writeScheduledRandomTicks(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<SerializableChunkData> cir) {
        List<BlockPos> scheduledRandomTicks = ((RandomTickScheduler) chunk).getScheduledRandomTicks();

        if (!scheduledRandomTicks.isEmpty()) {
            CompoundTag corgiLibTag = new CompoundTag();

            ListTag listTag = new ListTag();
            for (BlockPos scheduledRandomTick : scheduledRandomTicks) {
                listTag.add(new IntArrayTag(new int[]{scheduledRandomTick.getX(), scheduledRandomTick.getY(), scheduledRandomTick.getZ()}));
            }
            corgiLibTag.put("scheduled_random_ticks", listTag);

            cir.getReturnValue().structureData().put(Constants.MOD_ID, corgiLibTag);
        }
    }


    @Inject(method = "read", at = @At("RETURN"))
    private void readScheduledRandomTicks(ServerLevel level, PoiManager poiManager, RegionStorageInfo regionInfo, ChunkPos pos, CallbackInfoReturnable<ProtoChunk> cir) {
        CompoundTag tag = this.structureData;
        if (tag.contains(Constants.MOD_ID)) {
            Optional<CompoundTag> corgiLibTagOptional = tag.getCompound(Constants.MOD_ID);
            if (corgiLibTagOptional.isPresent()) {
                CompoundTag corgiLibTag = corgiLibTagOptional.get();
                if (corgiLibTag.contains("scheduled_random_ticks")) {
                    Optional<ListTag> listTagOptional = corgiLibTag.getList("scheduled_random_ticks");
                    if (listTagOptional.isPresent()) {
                        for (Tag scheduledTick : listTagOptional.get()) {
                            int[] intArrayTag = ((IntArrayTag) scheduledTick).getAsIntArray();
                            ((RandomTickScheduler) cir.getReturnValue()).getScheduledRandomTicks().add(new BlockPos(intArrayTag[0], intArrayTag[1], intArrayTag[2]));
                        }
                    }
                }
            }
        }
    }
}
