package com.yuankong.mod.lessfeatures.event;

import com.yuankong.mod.lessfeatures.init.FallWater;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FallEventHandle {


    @SubscribeEvent
    public static void toEntityFallEvent(LivingFallEvent event) {

        Entity entity = event.getEntity();
        //World world = entity.world;
        if (entity instanceof LivingEntity) {

            if(entity instanceof PlayerEntity){
                if(EntityEventHandle.noRidBoatFall.get(entity)==null){
                    if(FallWater.entityList.get(entity)==null){
                        if(FallWater.thisEntityDate.get(entity)!=null){
                            FallWater.thisEntityDate.remove(entity);
                        }
                        if(FallWater.thisEntityDate2.get(entity)!=null){
                            FallWater.thisEntityDate2.remove(entity);
                        }
                    }

                }
            }else{
                if(FallWater.thisEntityDate.get(entity)!=null){
                    FallWater.thisEntityDate.remove(entity);
                }
                if(FallWater.thisEntityDate2.get(entity)!=null){
                    FallWater.thisEntityDate2.remove(entity);
                }
            }

            if(FallWater.isNoRiddingToFall.get(entity)!=null){
                FallWater.isNoRiddingToFall.remove(entity);
            }

            if(event.getDistance()>5){
                FallWater.isNoRiddingToFall.put(entity,event.getDistance());
            }


        }
    }
}
