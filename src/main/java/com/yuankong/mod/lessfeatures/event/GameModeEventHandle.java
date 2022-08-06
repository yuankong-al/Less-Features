package com.yuankong.mod.lessfeatures.event;

import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GameModeEventHandle {

    @SubscribeEvent
    public static void toEntityDeathEvent(PlayerEvent.PlayerChangeGameModeEvent event) {

        Entity entity = event.getPlayer();
        if (FallWater.thisEntityDate.get(entity)!=null) {
            FallWater.thisEntityDate.remove(entity);
        }
        if (FallWater.thisEntityDate2.get(entity)!=null) {
            FallWater.thisEntityDate2.remove(entity);
        }

    }
}
