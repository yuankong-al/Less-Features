package com.yuankong.mod.lessfeatures.event;

import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathEventHandle {

    @SubscribeEvent
    public static void toEntityDeathEvent(LivingDeathEvent event) {

        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            FallWater.thisEntityDate.remove(entity);
            FallWater.thisEntityDate2.remove(entity);
        }
    }
}
