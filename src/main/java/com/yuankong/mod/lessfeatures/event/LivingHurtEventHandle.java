package com.yuankong.mod.lessfeatures.event;

import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHurtEventHandle {
        @SubscribeEvent
        public static void toLivingHurtEvent(LivingHurtEvent event) {

            Entity entity = event.getEntity();

            if(event.getSource() == DamageSource.FALL){
                if(FallWater.thisEntityDate.get(entity)!=null){
                    FallWater.thisEntityDate.remove(entity);
                }
                if(FallWater.thisEntityDate2.get(entity)!=null){
                    FallWater.thisEntityDate2.remove(entity);
                }
            }

        }

}
