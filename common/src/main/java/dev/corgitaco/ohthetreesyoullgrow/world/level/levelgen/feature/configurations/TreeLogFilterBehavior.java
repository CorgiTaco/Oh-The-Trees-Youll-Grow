package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;

public enum TreeLogFilterBehavior {
    PIERCE,
    PASSTHROUGH,
    BLOCK;


    public static final Codec<TreeLogFilterBehavior> CODEC = Codec.STRING.xmap(s -> TreeLogFilterBehavior.valueOf(s.toUpperCase()), s -> s.name().toUpperCase()); // Guards against case issues
}
