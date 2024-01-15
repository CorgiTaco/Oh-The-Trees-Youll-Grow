package dev.corgitaco.examplemod;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
public class ExampleModDataProvider {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {

    }
}
