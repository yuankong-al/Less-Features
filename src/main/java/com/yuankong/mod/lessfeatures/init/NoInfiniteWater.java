package com.yuankong.mod.lessfeatures.init;

import com.yuankong.mod.lessfeatures.config.LessFeaturesConfig;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NoInfiniteWater {

    @SubscribeEvent
    public static void toNoInfiniteWater(BlockEvent.CreateFluidSourceEvent event){
        if(!LessFeaturesConfig.IS_INFINITE_Water.get()){
            event.setResult(Event.Result.DENY);
        }

    }
}
