package com.yuankong.mod.lessfeatures.event;

import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityJoinWorldEventHandle {
    @SubscribeEvent
    public static void toEntityJoinWorldEvent(EntityJoinWorldEvent event) {

        Entity entity = event.getEntity();
        FallWater.thisEntityDate.remove(entity);
        FallWater.thisEntityDate2.remove(entity);
    }
}
