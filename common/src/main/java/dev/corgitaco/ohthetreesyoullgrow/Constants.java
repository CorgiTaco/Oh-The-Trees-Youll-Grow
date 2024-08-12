package dev.corgitaco.ohthetreesyoullgrow;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "ohthetreesyoullgrow";
	public static final String MOD_NAME = "Oh The Trees You'll Grow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation createLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}