package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.RandomTickScheduler;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfigV2;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TreeFromStructureNBTFeatureV2 extends Feature<TreeFromStructureNBTConfigV2> {

    private static final boolean DEBUG = false;

    public TreeFromStructureNBTFeatureV2(Codec<TreeFromStructureNBTConfigV2> $$0) {
        super($$0);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeFromStructureNBTConfigV2> featurePlaceContext) {
        TreeFromStructureNBTConfigV2 config = featurePlaceContext.config();

        BlockStateProvider logProvider = config.logProvider();
        List<BlockStateProvider> leavesProvider = config.leavesProvider();

        WorldGenLevel level = featurePlaceContext.level();
        StructureTemplateManager templateManager = level.getLevel().getStructureManager();
        ResourceLocation baseLocation = config.baseLocation();
        Optional<StructureTemplate> baseTemplateOptional = templateManager.get(baseLocation);
        ResourceLocation canopyLocation = config.canopyLocation();
        Optional<StructureTemplate> canopyTemplateOptional = templateManager.get(canopyLocation);

        if (baseTemplateOptional.isEmpty()) {
            throw noTreePartPresent(baseLocation);
        }
        if (canopyTemplateOptional.isEmpty()) {
            throw noTreePartPresent(canopyLocation);
        }
        StructureTemplate baseTemplate = baseTemplateOptional.get();
        StructureTemplate canopyTemplate = canopyTemplateOptional.get();
        List<StructureTemplate.Palette> basePalettes = baseTemplate.palettes;
        List<StructureTemplate.Palette> canopyPalettes = canopyTemplate.palettes;
        BlockPos origin = featurePlaceContext.origin();
        if (DEBUG) {
            level.setBlock(origin, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
            BlockPos blockPos = origin.above(10);
            level.setBlock(rotateInDirectionAroundOrigin(blockPos, origin, Direction.WEST), Blocks.REDSTONE_BLOCK.defaultBlockState(), 2);
            level.setBlock(rotateInDirectionAroundOrigin(blockPos, origin, Direction.EAST), Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
            level.setBlock(rotateInDirectionAroundOrigin(blockPos, origin, Direction.NORTH), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
            level.setBlock(rotateInDirectionAroundOrigin(blockPos, origin, Direction.SOUTH), Blocks.COPPER_BLOCK.defaultBlockState(), 2);
        }


        RandomSource random = featurePlaceContext.random();
        Rotation rotation = config.randomRotation() ? Rotation.getRandom(random) : Rotation.NONE;
        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotation(rotation);
        StructureTemplate.Palette trunkBasePalette = placeSettings.getRandomPalette(basePalettes, origin);
        StructureTemplate.Palette randomCanopyPalette = placeSettings.getRandomPalette(canopyPalettes, origin);

        List<StructureTemplate.StructureBlockInfo> center = trunkBasePalette.blocks(Blocks.WHITE_WOOL);

        if (center.isEmpty()) {
            throw new IllegalArgumentException("No trunk central position was specified for structure NBT palette %s. Trunk central position is specified with white wool.".formatted(config.baseLocation()));
        }
        if (center.size() > 1) {
            throw new IllegalArgumentException("There cannot be more than one trunk central position for structure NBT palette %s. Trunk central position is specified with white wool.".formatted(config.baseLocation()));
        }

        BlockPos centerOffset = center.getFirst().pos();
        centerOffset = new BlockPos(-centerOffset.getX(), 0, -centerOffset.getZ());


        List<StructureTemplate.StructureBlockInfo> logs = getStructureInfosInStructurePalletteFromBlockList(config.logTarget(), trunkBasePalette);
        List<StructureTemplate.StructureBlockInfo> logBuilders = trunkBasePalette.blocks(Blocks.RED_WOOL);
        if (logBuilders.isEmpty()) {
            throw new UnsupportedOperationException(String.format("\"%s\" is missing log builders.", baseLocation));
        }

        int trunkLength = config.height().sample(random);
        final int maxTrunkBuildingDepth = config.maxLogDepth();

        Direction direction = findDirectionOfGrowthFromOrientation(config.orientation(), config, logBuilders, placeSettings, centerOffset, level, origin, random);
        if (direction == null) {
            return false;
        }

        Map<BlockPos, BlockState> leavePositions = new HashMap<>();
        Map<BlockPos, BlockState> logPositions = new HashMap<>();
        Map<BlockPos, BlockState> additionalPositions = new HashMap<>();

        fillTrunkPositions(logProvider, leavesProvider, config, level, random, origin, placeSettings, trunkBasePalette, centerOffset, logs, logBuilders, leavePositions, logPositions, additionalPositions, maxTrunkBuildingDepth, direction);

        // Verify the canopy has connected with all trunk positions
        if (!fillCanopyPositions(trunkBasePalette.blocks(Blocks.YELLOW_WOOL), config, level, random, placeSettings, centerOffset, origin, randomCanopyPalette, leavePositions, logPositions, additionalPositions, trunkLength, direction)) {
            return false;
        }

        if (validateLogPositions(logPositions, config, level)) {
            return false; // Exit because some log positions are not valid.
        }


        if (insideStructure(logPositions, level, config)) {
            return false; // Exit because the trunk position intersects with a structure.
        }

        placeKnownBlockPositions(logPositions, level);
        placeKnownLeavePositions(leavePositions, level);
        placeKnownBlockPositions(additionalPositions, level);


        Set<BlockPos> decorationPositions = new HashSet<>();
        placeTreeDecorations(config.treeDecorators(), level, random, leavePositions.keySet(), logPositions.keySet(), decorationPositions);

        return true;
    }

    private static boolean doAllPositionsTouchGround(List<StructureTemplate.StructureBlockInfo> logBuilders, StructurePlaceSettings placeSettings, BlockPos centerOffset, BlockPos origin, TreeFromStructureNBTConfigV2 config, WorldGenLevel level, Direction direction) {
        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            pos = rotateInDirectionAroundOrigin(pos, origin, direction);
            if (!isOnGround(config.maxLogDepth(), level, pos, config.growableOn(), direction)) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static Direction findDirectionOfGrowthFromOrientation(TreeFromStructureNBTConfigV2.Orientation orientation, TreeFromStructureNBTConfigV2 config, List<StructureTemplate.StructureBlockInfo> logBuilders, StructurePlaceSettings placeSettings, BlockPos centerOffset, WorldGenLevel level, BlockPos origin, RandomSource random) {
        switch (orientation) {
            case STANDARD: {
                if (doAllPositionsTouchGround(logBuilders, placeSettings, centerOffset, origin, config, level, Direction.UP)) {
                    return Direction.UP;
                } else {
                    return null; // Unknown
                }
            }
            case UPSIDE_DOWN: {
                if (doAllPositionsTouchGround(logBuilders, placeSettings, centerOffset, origin, config, level, Direction.DOWN)) {
                    return Direction.DOWN;
                } else {
                    return null; // Unknown
                }
            }
            case SIDEWAYS: {
                List<Direction> horizontalDirections = Direction.Plane.HORIZONTAL.shuffledCopy(random);
                List<Direction> validDirections = new ArrayList<>(horizontalDirections.size());

                for (Direction direction : horizontalDirections) {
                    if (doAllPositionsTouchGround(logBuilders, placeSettings, centerOffset, origin, config, level, direction)) {
                        validDirections.add(direction);
                    }
                }
                if (!validDirections.isEmpty()) {
                    return validDirections.getFirst();
                }

                return null; // Unknown
            }
            default:
                throw new IllegalArgumentException("Unreachable statement, orientation %s is not supported.".formatted(orientation));
        }
    }

    private static boolean fillCanopyPositions(List<StructureTemplate.StructureBlockInfo> canopyAnchor, TreeFromStructureNBTConfigV2 config, WorldGenLevel level, RandomSource randomSource, StructurePlaceSettings placeSettings, BlockPos centerOffset, BlockPos origin, StructureTemplate.Palette randomCanopyPalette, Map<BlockPos, BlockState> leavePositions, Map<BlockPos, BlockState> logPositions, Map<BlockPos, BlockState> additionalPositions, int trunkLength, Direction treeGrowthDirection) {
        if (!canopyAnchor.isEmpty()) {
            if (canopyAnchor.size() > 1) {
                throw new IllegalArgumentException("There cannot be more than one central canopy position. Canopy central position is specified with yellow wool on the trunk palette.");
            }
            return fillCanopyPositions(config.logProvider(), config.leavesProvider(), config, level, randomSource, getModifiedPos(placeSettings, canopyAnchor.getFirst(), centerOffset, origin), placeSettings, randomCanopyPalette, leavePositions, logPositions, additionalPositions, trunkLength, treeGrowthDirection);
        } else {
            return fillCanopyPositions(config.logProvider(), config.leavesProvider(), config, level, randomSource, origin, placeSettings, randomCanopyPalette, leavePositions, logPositions, additionalPositions, trunkLength, treeGrowthDirection);
        }
    }


    private static boolean insideStructure(Map<BlockPos, BlockState> logPositions, WorldGenLevel level, TreeFromStructureNBTConfigV2 config) {
        if (level instanceof WorldGenRegion region) {
            for (BlockPos trunkPosition : logPositions.keySet()) {
                ChunkAccess chunk = level.getChunk(trunkPosition);
                for (StructureStart value : chunk.getAllStarts().values()) {
                    for (StructurePiece piece : value.getPieces()) {
                        if (piece.getBoundingBox().isInside(trunkPosition) && !config.logsPlacementFilter().test(level, trunkPosition)) {
                            return true;
                        }
                    }
                }

                for (Map.Entry<Structure, LongSet> entry : chunk.getAllReferences().entrySet()) {
                    Structure structure = entry.getKey();
                    LongSet references = entry.getValue();
                    for (long reference : references) {
                        int chunkX = ChunkPos.getX(reference);
                        int chunkZ = ChunkPos.getZ(reference);
                        if (!region.hasChunk(chunkX, chunkZ)) {
                            continue;
                        }
                        ChunkAccess referenceChunk = region.getChunk(chunkX, chunkZ, ChunkStatus.STRUCTURE_STARTS, true);

                        StructureStart startForStructure = referenceChunk.getStartForStructure(structure);
                        if (startForStructure != null) {
                            for (StructurePiece piece : startForStructure.getPieces()) {
                                if (piece.getBoundingBox().isInside(trunkPosition) && !config.logsPlacementFilter().test(level, trunkPosition)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean validateLogPositions(Map<BlockPos, BlockState> logPositions, TreeFromStructureNBTConfigV2 config, WorldGenLevel level) {
        for (BlockPos trunkPosition : logPositions.keySet()) {
            if (!config.logsPlacementFilter().test(level, trunkPosition)) {
                switch (config.treeLogFilterBehavior()) {
                    case PIERCE:
                        continue;
                    case PASSTHROUGH:
                        logPositions.remove(trunkPosition);
                    case BLOCK:
                        return true;
                }
            }
        }
        return false;
    }

    private static void placeKnownBlockPositions(Map<BlockPos, BlockState> trunkPositions, WorldGenLevel level) {
        for (Map.Entry<BlockPos, BlockState> entry : trunkPositions.entrySet()) {
            BlockPos trunkPosition = entry.getKey();
            BlockState state = entry.getValue();
            level.setBlock(trunkPosition, state, 2);
        }
    }

    private static void placeKnownLeavePositions(Map<BlockPos, BlockState> leavePositions, WorldGenLevel level) {
        List<Runnable> leavesPostApply = new ArrayList<>(leavePositions.size());

        for (Map.Entry<BlockPos, BlockState> entry : leavePositions.entrySet()) {
            BlockPos leavePosition = entry.getKey();
            BlockState state = entry.getValue();
            level.setBlock(leavePosition, state, 2);
            if (state.hasProperty(LeavesBlock.DISTANCE)) {
                Runnable postProcess = () -> {
                    BlockState blockState = LeavesBlock.updateDistance(state, level, leavePosition);
                    if (blockState.getValue(LeavesBlock.DISTANCE) < LeavesBlock.DECAY_DISTANCE) {
                        if (blockState.hasProperty(LeavesBlock.PERSISTENT)) {
                            blockState = blockState.setValue(LeavesBlock.PERSISTENT, false);
                        }
                        level.setBlock(leavePosition, blockState, 2);
                        level.scheduleTick(leavePosition, blockState.getBlock(), 0);
                    } else {
                        level.removeBlock(leavePosition, false);
                        leavePositions.remove(leavePosition.immutable());
                    }
                };
                leavesPostApply.add(postProcess);
            }
        }

        leavesPostApply.forEach(Runnable::run);
    }

    public static void fillTrunkPositions(BlockStateProvider logProvider, List<BlockStateProvider> leavesProvider, TreeFromStructureNBTConfigV2 config, WorldGenLevel level, RandomSource randomSource, BlockPos origin, StructurePlaceSettings placeSettings, StructureTemplate.Palette trunkBasePalette, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logs, List<StructureTemplate.StructureBlockInfo> logBuilders, Map<BlockPos, BlockState> leavePositions, Map<BlockPos, BlockState> trunkPositions, Map<BlockPos, BlockState> additionalBlocks, int maxTrunkBuildingDepth, Direction treeGrowthDirection) {
        fillLogsUnder(logProvider, level, randomSource, origin, placeSettings, centerOffset, logBuilders, maxTrunkBuildingDepth, config.growableOn(), trunkPositions, treeGrowthDirection);
        placeLogsWithRotation(logProvider, level, randomSource, origin, placeSettings, centerOffset, logs, trunkPositions, treeGrowthDirection);
        placeLeavesWithCalculatedDistanceAndRotation(leavesProvider, level, origin, randomSource, placeSettings, getStructureInfosInStructurePalletteFromBlockListV2(config.leavesTarget(), trunkBasePalette), leavePositions, centerOffset, config.leavesPlacementFilter(), treeGrowthDirection);
        Map<Block, BlockStateProvider> replaceFromNBT = config.replaceFromNBT();

        replaceFromNBT.forEach((old, newBlock) -> {
            List<StructureTemplate.StructureBlockInfo> additionalBlocksInfo = getStructureInfosInStructurePalletteFromBlockList(List.of(old), trunkBasePalette);
            for (StructureTemplate.StructureBlockInfo additionalBlock : additionalBlocksInfo) {
                BlockPos pos = getModifiedPos(placeSettings, additionalBlock, centerOffset, origin);
                pos = rotateInDirectionAroundOrigin(pos, origin, treeGrowthDirection);
                additionalBlocks.put(pos.immutable(), getTransformedState(pos, newBlock.getState(randomSource, pos), additionalBlock.state(), placeSettings.getRotation(), level, treeGrowthDirection));
                ((RandomTickScheduler) level.getChunk(pos)).scheduleRandomTick(pos.immutable());
            }
        });
    }

    public static boolean fillCanopyPositions(BlockStateProvider logProvider, List<BlockStateProvider> leavesProvider, TreeFromStructureNBTConfigV2 config, WorldGenLevel level, RandomSource randomSource, BlockPos origin, StructurePlaceSettings placeSettings, StructureTemplate.Palette randomCanopyPalette, Map<BlockPos, BlockState> leavePositions, Map<BlockPos, BlockState> trunkPositions, Map<BlockPos, BlockState> additionalBlocks, int trunkLength, Direction treeGrowthDirection) {
        List<List<StructureTemplate.StructureBlockInfo>> leaves = getStructureInfosInStructurePalletteFromBlockListV2(config.leavesTarget(), randomCanopyPalette);
        List<StructureTemplate.StructureBlockInfo> canopyLogs = getStructureInfosInStructurePalletteFromBlockList(config.logTarget(), randomCanopyPalette);
        List<StructureTemplate.StructureBlockInfo> canopyAnchor = randomCanopyPalette.blocks(Blocks.WHITE_WOOL);

        if (canopyAnchor.isEmpty()) {
            throw new IllegalArgumentException("No canopy anchor was specified for structure NBT palette %s. Canopy anchor is specified with white wool.".formatted(config.canopyLocation()));
        }
        if (canopyAnchor.size() > 1) {
            throw new IllegalArgumentException("There cannot be more than one canopy anchor for structure NBT palette %s. Canopy anchor is specified with white wool.".formatted(config.canopyLocation()));
        }

        StructureTemplate.StructureBlockInfo structureBlockInfo = canopyAnchor.getFirst();
        BlockPos canopyCenterOffset = structureBlockInfo.pos();
        canopyCenterOffset = new BlockPos(-canopyCenterOffset.getX(), trunkLength, -canopyCenterOffset.getZ());

        List<StructureTemplate.StructureBlockInfo> trunkFillers = new ArrayList<>(randomCanopyPalette.blocks(Blocks.RED_WOOL));

        if (!intersectTrunk(logProvider, level, randomSource, origin, placeSettings, canopyCenterOffset, trunkFillers, trunkLength + 1, trunkPositions, treeGrowthDirection)) {
            return false;
        }

        placeLogsWithRotation(logProvider, level, randomSource, origin, placeSettings, canopyCenterOffset, canopyLogs, trunkPositions, treeGrowthDirection);
        placeLeavesWithCalculatedDistanceAndRotation(leavesProvider, level, origin, randomSource, placeSettings, leaves, leavePositions, canopyCenterOffset, config.leavesPlacementFilter(), treeGrowthDirection);
        Map<Block, BlockStateProvider> replaceFromNBT = config.replaceFromNBT();
        BlockPos finalCanopyCenterOffset = canopyCenterOffset;
        replaceFromNBT.forEach((old, newBlock) -> {
            List<StructureTemplate.StructureBlockInfo> additionalBlocksInfo = getStructureInfosInStructurePalletteFromBlockList(List.of(old), randomCanopyPalette);
            for (StructureTemplate.StructureBlockInfo additionalBlock : additionalBlocksInfo) {
                BlockPos pos = getModifiedPos(placeSettings, additionalBlock, finalCanopyCenterOffset, origin);
                pos = rotateInDirectionAroundOrigin(pos, origin, treeGrowthDirection);
                additionalBlocks.put(pos.immutable(), getTransformedState(pos, newBlock.getState(randomSource, pos), additionalBlock.state(), placeSettings.getRotation(), level, treeGrowthDirection));
                ((RandomTickScheduler) level.getChunk(pos)).scheduleRandomTick(pos.immutable());
            }
        });

        return true;
    }

    public static void placeLogsWithRotation(BlockStateProvider logProvider, WorldGenLevel level, RandomSource random, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logs, Map<BlockPos, BlockState> trunkPositions, Direction treeGrowthDirection) {
        for (StructureTemplate.StructureBlockInfo trunk : logs) {
            BlockPos pos = getModifiedPos(placeSettings, trunk, centerOffset, origin);
            pos = rotateInDirectionAroundOrigin(pos, origin, treeGrowthDirection);
            trunkPositions.put(pos.immutable(), getTransformedState(pos, logProvider.getState(random, pos), trunk.state(), placeSettings.getRotation(), level, treeGrowthDirection));
        }
    }

    public static void placeTreeDecorations(Iterable<TreeDecorator> treeDecorators, WorldGenLevel level, RandomSource random, Set<BlockPos> leavePositions, Set<BlockPos> trunkPositions, Set<BlockPos> decorationPositions) {
        for (TreeDecorator treeDecorator : treeDecorators) {
            treeDecorator.place(new TreeDecorator.Context(level, (pos, state) -> {
                level.setBlock(pos, state, 2);
                decorationPositions.add(pos.immutable());
            }, random, trunkPositions, leavePositions, trunkPositions));
        }
    }

    public static void placeLeavesWithCalculatedDistanceAndRotation(List<BlockStateProvider> leavesProvider, WorldGenLevel level, BlockPos origin, RandomSource random, StructurePlaceSettings placeSettings, List<List<StructureTemplate.StructureBlockInfo>> leaves, Map<BlockPos, BlockState> leavePositions, BlockPos canopyCenterOffset, BlockPredicate leavesPlacementFilter, Direction treeGrowthDirection) {
        for (int i = 0; i < leaves.size(); i++) {
            List<StructureTemplate.StructureBlockInfo> meow = leaves.get(i);
            for (StructureTemplate.StructureBlockInfo leaf : meow) {
                BlockPos modifiedPos = getModifiedPos(placeSettings, leaf, canopyCenterOffset, origin);
                modifiedPos = rotateInDirectionAroundOrigin(modifiedPos, origin, treeGrowthDirection);

                if (leavesPlacementFilter.test(level, modifiedPos)) {
                    leavePositions.put(modifiedPos.immutable(), getTransformedState(modifiedPos, leavesProvider.get(i).getState(random, modifiedPos), leaf.state(), placeSettings.getRotation(), level, treeGrowthDirection));
                }
            }
        }
    }

    public static void fillLogsUnder(BlockStateProvider logProvider, WorldGenLevel level, RandomSource random, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logBuilders, int maxTrunkBuildingDepth, BlockPredicate groundFilter, Map<BlockPos, BlockState> trunkPositions, Direction treeGrowthDirection) {
        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            pos = rotateInDirectionAroundOrigin(pos, origin, treeGrowthDirection);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);

            for (int i = 0; i < maxTrunkBuildingDepth; i++) {
                if (!groundFilter.test(level, mutableBlockPos) && !level.getBlockState(mutableBlockPos).is(Blocks.BEDROCK)) {
                    trunkPositions.put(mutableBlockPos.immutable(), getTransformedState(mutableBlockPos, logProvider.getState(random, mutableBlockPos), logBuilder.state(), placeSettings.getRotation(), level, treeGrowthDirection));
                    mutableBlockPos.move(treeGrowthDirection.getOpposite());
                } else {
                    ((RandomTickScheduler) level.getChunk(mutableBlockPos)).scheduleRandomTick(mutableBlockPos.immutable());
                    break;
                }
            }
        }
    }

    public static boolean intersectTrunk(BlockStateProvider logProvider, WorldGenLevel level, RandomSource random, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logBuilders, int maxTrunkBuildingDepth, Map<BlockPos, BlockState> trunkPositions, Direction treeGrowthDirection) {
        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            pos = rotateInDirectionAroundOrigin(pos, origin, treeGrowthDirection);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);

            for (int i = 0; i <= maxTrunkBuildingDepth; i++) {
                if (!trunkPositions.containsKey(mutableBlockPos)) {
                    trunkPositions.put(mutableBlockPos.immutable(), getTransformedState(mutableBlockPos, logProvider.getState(random, mutableBlockPos), logBuilder.state(), placeSettings.getRotation(), level, treeGrowthDirection));
                    mutableBlockPos.move(treeGrowthDirection.getOpposite());
                } else {
                    break;
                }

                if (i == maxTrunkBuildingDepth) {
                    return false;
                }
            }
        }

        return true;
    }


    @NotNull
    public static BlockState getTransformedState(BlockPos modifiedPos, BlockState state, BlockState nbtState, Rotation rotation, WorldGenLevel level, Direction directionOfGrowth) {
        for (BlockStateModifier modifier : BLOCK_STATE_MODIFIERS) {
            state = modifier.apply(modifiedPos, state, nbtState, rotation, level, directionOfGrowth);
        }
        return state;
    }

    public static boolean isOnGround(int maxLogDepth, WorldGenLevel level, BlockPos pos, BlockPredicate growableOn, Direction treeGrowthDirection) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        for (int logDepth = 0; logDepth < maxLogDepth; logDepth++) {
            mutableBlockPos.move(treeGrowthDirection.getOpposite());
            if (growableOn.test(level, mutableBlockPos)) {
                return true;
            }
        }

        return false;
    }

    public static BlockPos getModifiedPos(StructurePlaceSettings settings, StructureTemplate.StructureBlockInfo placing, BlockPos partCenter, BlockPos featureOrigin) {
        return StructureTemplate.calculateRelativePosition(settings, placing.pos()).offset(featureOrigin).offset(StructureTemplate.calculateRelativePosition(settings, partCenter));
    }


    public static BlockPos rotateInDirectionAroundOrigin(BlockPos pos, BlockPos origin, Direction direction) {
        BlockPos offset = pos.subtract(origin);
        return switch (direction) {
            case UP -> pos;
            case DOWN ->
                    new BlockPos(origin.getX() + offset.getX(), origin.getY() - offset.getY(), origin.getZ() + offset.getZ());
            case EAST ->
                    new BlockPos(origin.getX() + offset.getY(), origin.getY() - offset.getX(), origin.getZ() + offset.getZ());
            case WEST ->
                    new BlockPos(origin.getX() - offset.getY(), origin.getY() + offset.getX(), origin.getZ() + offset.getZ());
            case NORTH ->
                    new BlockPos(origin.getX() + offset.getX(), origin.getY() - offset.getZ(), origin.getZ() - offset.getY());
            case SOUTH ->
                    new BlockPos(origin.getX() + offset.getX(), origin.getY() + offset.getZ(), origin.getZ() + offset.getY());
        };
    }

    public static IllegalArgumentException noTreePartPresent(ResourceLocation location) {
        return new IllegalArgumentException(String.format("\"%s\" is not a valid tree part.", location));
    }

    public static List<StructureTemplate.StructureBlockInfo> getStructureInfosInStructurePalletteFromBlockList(Iterable<Block> blocks, StructureTemplate.Palette palette) {
        List<StructureTemplate.StructureBlockInfo> result = new ArrayList<>();
        for (Block block : blocks) {
            result.addAll(palette.blocks(block));
        }
        return result;
    }

    public static List<List<StructureTemplate.StructureBlockInfo>> getStructureInfosInStructurePalletteFromBlockListV2(Iterable<Block> blocks, StructureTemplate.Palette palette) {
        List<List<StructureTemplate.StructureBlockInfo>> result = new ArrayList<>();
        for (Block block : blocks) {
            result.add(palette.blocks(block));
        }
        return result;
    }

    public static final List<BlockStateModifier> BLOCK_STATE_MODIFIERS = Util.make(new ArrayList<>(), list -> {
        list.add((modifiedPos, state, nbtState, rotation, level, directionOfGrowth) -> {
            for (Property property : state.getProperties()) {
                if (nbtState.hasProperty(property)) {
                    Comparable value = nbtState.getValue(property);
                    state = state.setValue(property, value);
                }
            }

            return state;
        });
        list.add((modifiedPos, lastState, nbtState, rotation, level, directionOfGrowth) -> {
            if (lastState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                FluidState fluidState = level.getFluidState(modifiedPos);
                if (fluidState.is(Fluids.WATER) && fluidState.getAmount() >= 7) {
                    lastState = lastState.setValue(BlockStateProperties.WATERLOGGED, true);
                } else {
                    lastState = lastState.setValue(BlockStateProperties.WATERLOGGED, false);
                }
            }
            return lastState;
        });
        list.add((modifiedPos, lastState, nbtState, rotation, level, directionOfGrowth) -> lastState.rotate(rotation));
        list.add((modifiedPos, lastState, nbtState, rotation, level, directionOfGrowth) -> {
            if (lastState.hasProperty(BlockStateProperties.AXIS)) {
                Direction.Axis axis = lastState.getValue(BlockStateProperties.AXIS);
                Direction.Axis directionOfGrowthAxis = directionOfGrowth.getAxis();
                switch (directionOfGrowthAxis) {
                    case Y -> {
                        // No change needed
                    }
                    case X -> {
                        if (axis == Direction.Axis.Y) {
                            lastState = lastState.setValue(BlockStateProperties.AXIS, Direction.Axis.X);
                        } else if (axis == Direction.Axis.X) {
                            lastState = lastState.setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
                        }
                    }
                    case Z -> {
                        if (axis == Direction.Axis.Y) {
                            lastState = lastState.setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
                        } else if (axis == Direction.Axis.Z) {
                            lastState = lastState.setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
                        }
                    }
                }
            }
            return lastState;
        });
    });

    @FunctionalInterface
    public interface BlockStateModifier {
        BlockState apply(BlockPos modifiedPos, BlockState lastState, BlockState nbtState, Rotation rotation, WorldGenLevel level, Direction directionOfGrowth);
    }
}
