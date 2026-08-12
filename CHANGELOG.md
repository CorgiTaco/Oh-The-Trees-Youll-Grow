# 8.1.0
* Add sideways and upside down tree config choices.
* Fix canopy and trunk additional block placements.
* Allow BlockStateProperties.FACING to be used on AttachedToLogsDecorator and still set Direction correctly.
* Add the ability to use blocks within the NBT to place Blockstates via Blockstate providers.
* Allow the ability to specify multiple leave targets and multiple leave block state providers.
* Introduce Tree From Structure NBT v2
* Fix placing additional blocks from NBT in the v1 feature
* Fix issue with piercing walls and structures and bedrock
* Add the ability to define the log filter behavior. Filter options include: PIERCE - Destroy blocks in the filter. PASSTHROUGH - Tree will generate but skip blocks in filter. BLOCK - Tree will not generate if it meets a block in the filter.
* Add the ability to turn on/off random tree rotation.

# 8.0.3
* Allow BlockStateProperties.FACING to be used on AttachedToLogsDecorator and still set Direction correctly.

# 8.0.2
- Mark as 1.21.10/.9 compatible.

# 8.0.1
* Fix mixin target when reading chunk data to be on return for random scheduled ticks.

# 8.0.0
- Update to 1.21.8

# 7.0.0
- Update to 1.21.6/1.21.7

# 6.0.8
* Concurrency safety for random scheduled ticks.

# 6.0.7
* Fix crash pertaining to calling chunks out of range.

# 6.0.6
* Add structure checks to tree growth during world generation.

# 6.0.5
* Add is sapling check. Fixes tree density issues.

# 6.0.4
* Fix trunks.

# 6.0.3
* Fix log decorators placing on leaves.
* Add more checks to fail tree growth.

# 6.0.2
Fix placing tree decorator blocks.

# 6.0.1
* Rewrite tree growth logic to account for solid blocks and the surrounding environment prior to growing/placing.

# 6.0.0
* Port to 1.21.4

# 5.0.6
* Track tree decoration positions when placing tree feature.

# 5.0.5
* Fix missing trunk/leave positions in tree decorators.

# 5.0.4
* Fix adding and sort trunk/leave positions and leaves to their respective sets. Fixes various issues with tree decorators.

# 5.0.3
* Fix canopy log builders piercing terrain

# 5.0.2
* Fix NeoForge Jar Missing AccessTransformer
* Make TREEFROMSTRUCTURENBT final
* Fix Deprecation of FMLJavaModLoadingContext.get in forge
* Adjust some Publishing Names

# 5.0.1
* Add check to prevent logs from being placed where bedrock is present

# 5.0.0
* Update to 1.21.1

# 4.0.0
* Update to 1.20.6
* Move Datagen to NeoForge

# 3.0.0
* Update to 1.20.4

# 2.0.0
* Update to 1.20.2
* Add NeoForge Support
* Remove CorgiLib Legacy Support

# 1.3.0
* Rewrite Build Script
* Generalize Registration
* Add TreeFromStructureNBTConfig constructor that has an empty tree decorator

# 1.2.0
* Move project to Arch Loom
* Add rotated block support
* Add example mushroom features

# 1.1.2
* Fix Tree Decorator Types not being registered on Fabric
* Use ForgeRegistries for Registration on Forge

# 1.1.1
* Fix incorrect Fabric version.

# 1.1.0
* Remove Regutils dependency

# 1.0.0
* Release
